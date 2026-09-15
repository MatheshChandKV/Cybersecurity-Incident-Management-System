package com.cims;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthService authService = new AuthService();
    private static final UserDAO userDAO = new UserDAO();
    private static final IncidentDAO incidentDAO = new IncidentDAO();
    private static final InvestigationDAO investigationDAO = new InvestigationDAO();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n================================");
            System.out.println("CYBERSECURITY INCIDENT SYSTEM");
            System.out.println("================================");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    try {
                        User user = loginFlow();
                        if (user != null) {
                            switch (user.getRole()) {
                                case USER -> userMenu(user);
                                case ANALYST -> analystMenu(user);
                                case ADMIN -> adminMenu(user);
                            }
                        }
                    } catch (SQLException e) {
                        System.err.println("Database error: " + e.getMessage());
                    }
                    break;
                case "2":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static User loginFlow() throws SQLException {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        try {
            User user = authService.login(username, password);
            incidentDAO.logAction(user.getId(), "LOGIN", null);
            System.out.println("Login successful! Role: " + user.getRole());
            return user;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static void userMenu(User user) throws SQLException {
        while (true) {
            System.out.println("\n============== USER MENU ==============");
            System.out.println("1. Report Incident");
            System.out.println("2. View My Incidents");
            System.out.println("3. Logout");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> reportIncident(user);
                case "2" -> viewMyIncidents(user);
                case "3" -> {
                    System.out.println("Logging out.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void reportIncident(User user) throws SQLException {
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Description: ");
        String desc = scanner.nextLine().trim();

        IncidentType type = null;
        while (type == null) {
            System.out.println("Select Incident Type:");
            for (int i = 0; i < IncidentType.values().length; i++) {
                System.out.printf("%d. %s%n", i + 1, IncidentType.values()[i]);
            }
            System.out.print("Choice: ");
            String tChoice = scanner.nextLine().trim();
            try {
                int idx = Integer.parseInt(tChoice) - 1;
                type = IncidentType.values()[idx];
            } catch (Exception e) {
                System.out.println("Invalid selection.");
            }
        }

        int impact = readFactor("Impact (1‑5): ");
        int likelihood = readFactor("Likelihood (1‑5): ");
        int exposure = readFactor("Exposure (1‑5): ");

        int riskScore = RiskCalculator.calculateRisk(impact, likelihood, exposure);
        String severity = RiskCalculator.determineSeverity(riskScore);

        Incident incident = new Incident(title, desc, type,
                impact, likelihood, exposure, user.getId());
        incident.setRiskScore(riskScore);
        incident.setSeverity(severity);
        incident.setStatus(IncidentStatus.REPORTED);

        int incidentId = incidentDAO.createIncident(incident);
        incidentDAO.logAction(user.getId(), "INCIDENT_CREATED", incidentId);

        System.out.println("\nIncident created successfully!");
        System.out.println("Incident ID: " + incidentId);
        System.out.println("Risk Score: " + riskScore);
        System.out.println("Severity   : " + severity);
    }

    private static int readFactor(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(input);
                if (val >= 1 && val <= 5)
                    return val;
                System.out.println("Value must be between 1 and 5.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    private static void viewMyIncidents(User user) throws SQLException {
        List<Incident> incidents = incidentDAO.getIncidentsByUser(user.getId());
        if (incidents.isEmpty()) {
            System.out.println("No incidents reported yet.");
            return;
        }
        System.out.println("\n--- Your Incidents ---");
        for (Incident inc : incidents) {
            System.out.printf("ID:%d | Title:%s | Type:%s | Risk:%d | Sev:%s | Status:%s | Assigned Analyst:%s%n",
                    inc.getId(), inc.getTitle(), inc.getType(),
                    inc.getRiskScore(), inc.getSeverity(),
                    inc.getStatus(), inc.getAssignedTo() == null ? "None" : inc.getAssignedTo());
        }
    }

    private static void analystMenu(User analyst) throws SQLException {
        while (true) {
            System.out.println("\n============== ANALYST MENU ==============");
            System.out.println("1. View Assigned Incidents");
            System.out.println("2. Update Incident Status");
            System.out.println("3. Add Investigation");
            System.out.println("4. View Priority Incidents");
            System.out.println("5. Logout");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> viewAssignedIncidents(analyst);
                case "2" -> updateIncidentStatus(analyst);
                case "3" -> addInvestigation(analyst);
                case "4" -> viewPriorityIncidents();
                case "5" -> {
                    System.out.println("Logging out.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void viewAssignedIncidents(User analyst) throws SQLException {
        List<Incident> incidents = incidentDAO.getIncidentsByAnalyst(analyst.getId());
        if (incidents.isEmpty()) {
            System.out.println("No incidents assigned to you.");
            return;
        }
        System.out.println("\n--- Assigned Incidents ---");
        for (Incident inc : incidents) {
            System.out.printf("ID:%d | Title:%s | Type:%s | Risk:%d | Sev:%s | Status:%s%n",
                    inc.getId(), inc.getTitle(), inc.getType(),
                    inc.getRiskScore(), inc.getSeverity(),
                    inc.getStatus());
        }
    }

    private static void updateIncidentStatus(User analyst) throws SQLException {
        System.out.print("Enter Incident ID to update: ");
        try {
            int incId = Integer.parseInt(scanner.nextLine().trim());

            Incident inc = incidentDAO.getIncidentById(incId);
            if (inc == null || inc.getAssignedTo() == null || inc.getAssignedTo() != analyst.getId()) {
                System.out.println("Incident not found or not assigned to you.");
                return;
            }

            System.out.println("Current status: " + inc.getStatus());
            System.out.println("Select new status:");
            System.out.println("1. OPEN");
            System.out.println("2. INVESTIGATING");
            System.out.println("3. RESOLVED");
            System.out.print("Choice: ");
            String s = scanner.nextLine().trim();

            IncidentStatus newStatus;
            switch (s) {
                case "1" -> newStatus = IncidentStatus.OPEN;
                case "2" -> newStatus = IncidentStatus.INVESTIGATING;
                case "3" -> newStatus = IncidentStatus.RESOLVED;
                default -> {
                    System.out.println("Invalid status choice.");
                    return;
                }
            }

            incidentDAO.updateStatus(incId, newStatus);
            incidentDAO.logAction(analyst.getId(), "STATUS_UPDATED", incId);
            System.out.println("Status updated.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private static void addInvestigation(User analyst) throws SQLException {
        System.out.print("Incident ID: ");
        try {
            int incId = Integer.parseInt(scanner.nextLine().trim());

            Incident inc = incidentDAO.getIncidentById(incId);
            if (inc == null || inc.getAssignedTo() == null || inc.getAssignedTo() != analyst.getId()) {
                System.out.println("Incident not found or not assigned to you.");
                return;
            }

            System.out.print("Findings: ");
            String findings = scanner.nextLine().trim();
            System.out.print("Actions taken: ");
            String actions = scanner.nextLine().trim();

            Investigation inv = new Investigation(incId, analyst.getId(), findings, actions);
            investigationDAO.addInvestigation(inv);
            incidentDAO.logAction(analyst.getId(), "INVESTIGATION_ADDED", incId);
            System.out.println("Investigation recorded.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private static void viewPriorityIncidents() throws SQLException {
        List<Incident> all = incidentDAO.getAllIncidentsForPriority();
        if (all.isEmpty()) {
            System.out.println("No incidents in system.");
            return;
        }
        PriorityQueue<Incident> pq = new PriorityQueue<>(
                Comparator.comparingInt(Incident::getRiskScore).reversed());
        pq.addAll(all);

        System.out.println("\n--- Incidents by Priority (high → low) ---");
        while (!pq.isEmpty()) {
            Incident inc = pq.poll();
            System.out.printf("ID:%d | Title:%s | Type:%s | Risk:%d | Sev:%s | Status:%s%n",
                    inc.getId(), inc.getTitle(), inc.getType(),
                    inc.getRiskScore(), inc.getSeverity(),
                    inc.getStatus());
        }
    }

    private static void adminMenu(User admin) throws SQLException {
        while (true) {
            System.out.println("\n============== ADMIN MENU ==============");
            System.out.println("1. View All Incidents");
            System.out.println("2. Assign Analyst");
            System.out.println("3. View Users");
            System.out.println("4. View Reports");
            System.out.println("5. View Audit Logs");
            System.out.println("6. Logout");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> viewAllIncidents();
                case "2" -> assignAnalyst(admin);
                case "3" -> viewAllUsers();
                case "4" -> viewReports();
                case "5" -> viewAuditLogs();
                case "6" -> {
                    System.out.println("Logging out.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void viewAllIncidents() throws SQLException {
        List<Incident> incidents = incidentDAO.getAllIncidents();
        if (incidents.isEmpty()) {
            System.out.println("No incidents in system.");
            return;
        }
        System.out.println("\n--- All Incidents ---");
        for (Incident inc : incidents) {
            System.out.printf("ID:%d | Title:%s | Type:%s | Risk:%d | Sev:%s | Status:%s | Reporter:%d | Analyst:%s%n",
                    inc.getId(), inc.getTitle(), inc.getType(),
                    inc.getRiskScore(), inc.getSeverity(),
                    inc.getStatus(), inc.getReportedBy(),
                    inc.getAssignedTo() == null ? "None" : inc.getAssignedTo());
        }
    }

    private static void assignAnalyst(User admin) throws SQLException {
        System.out.print("Incident ID to assign: ");
        try {
            int incId = Integer.parseInt(scanner.nextLine().trim());

            Incident inc = incidentDAO.getIncidentById(incId);
            if (inc == null) {
                System.out.println("Incident not found.");
                return;
            }

            List<User> analysts = userDAO.getAnalysts();
            if (analysts.isEmpty()) {
                System.out.println("No analysts in system.");
                return;
            }

            System.out.println("Available Analysts:");
            for (User a : analysts) {
                System.out.printf("%d. %s (%s)%n", a.getId(), a.getName(), a.getUsername());
            }
            System.out.print("Enter Analyst ID: ");
            int analystId = Integer.parseInt(scanner.nextLine().trim());

            boolean ok = analysts.stream().anyMatch(u -> u.getId() == analystId);
            if (!ok) {
                System.out.println("Invalid analyst ID.");
                return;
            }

            incidentDAO.assignAnalyst(incId, analystId);
            incidentDAO.logAction(admin.getId(), "ANALYST_ASSIGNED", incId);
            System.out.println("Analyst assigned.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private static void viewAllUsers() throws SQLException {
        List<User> users = userDAO.getAllUsers();
        System.out.println("\n--- Users ---");
        for (User u : users) {
            System.out.printf("ID:%d | Name:%s | Username:%s | Role:%s%n",
                    u.getId(), u.getName(), u.getUsername(), u.getRole());
        }
    }

    private static void viewReports() throws SQLException {
        IncidentDAO.ReportCounts rc = incidentDAO.getReportCounts();

        System.out.println("\n--- Reports ---");

        int total = rc.statusCounts.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println("Total Incidents: " + total);

        System.out.println("\nIncidents by Status:");
        rc.statusCounts.forEach((k, v) -> System.out.println(k + ": " + v));

        System.out.println("\nIncidents by Severity:");
        rc.severityCounts.forEach((k, v) -> System.out.println(k + ": " + v));

        System.out.println("\nIncidents by Type:");
        rc.typeCounts.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    private static void viewAuditLogs() throws SQLException {
        String sql = "SELECT a.id, u.username, a.action, a.incident_id, a.created_at " +
                "FROM audit_logs a JOIN users u ON a.user_id = u.id " +
                "ORDER BY a.created_at DESC LIMIT 50";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            System.out.println("\n--- Audit Logs (most recent 50) ---");
            while (rs.next()) {
                String username = rs.getString("username");
                String action = rs.getString("action");
                Integer incidentId = rs.getObject("incident_id") != null ? rs.getInt("incident_id") : null;
                Timestamp ts = rs.getTimestamp("created_at");
                System.out.printf("User:%s | Action:%s | Incident:%s | Time:%s%n",
                        username, action,
                        incidentId == null ? "N/A" : incidentId,
                        ts.toLocalDateTime());
            }
        }
    }
}
