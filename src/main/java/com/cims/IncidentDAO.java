package com.cims;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IncidentDAO {

    public int createIncident(Incident incident) throws SQLException {
        String sql = "INSERT INTO incidents (title, description, type, impact, likelihood, exposure, " +
                "risk_score, severity, status, reported_by, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, incident.getTitle());
            ps.setString(2, incident.getDescription());
            ps.setString(3, incident.getType().name());
            ps.setInt(4, incident.getImpact());
            ps.setInt(5, incident.getLikelihood());
            ps.setInt(6, incident.getExposure());
            ps.setInt(7, incident.getRiskScore());
            ps.setString(8, incident.getSeverity());
            ps.setString(9, incident.getStatus().name());
            ps.setInt(10, incident.getReportedBy());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to insert incident.");
    }

    public Incident getIncidentById(int id) throws SQLException {
        String sql = "SELECT * FROM incidents WHERE id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public List<Incident> getIncidentsByUser(int userId) throws SQLException {
        List<Incident> list = new ArrayList<>();
        String sql = "SELECT * FROM incidents WHERE reported_by = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public List<Incident> getIncidentsByAnalyst(int analystId) throws SQLException {
        List<Incident> list = new ArrayList<>();
        String sql = "SELECT * FROM incidents WHERE assigned_to = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, analystId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public List<Incident> getAllIncidents() throws SQLException {
        List<Incident> list = new ArrayList<>();
        String sql = "SELECT * FROM incidents";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public void assignAnalyst(int incidentId, int analystId) throws SQLException {
        String sql = "UPDATE incidents SET assigned_to = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, analystId);
            ps.setInt(2, incidentId);
            ps.executeUpdate();
        }
    }

    public void updateStatus(int incidentId, IncidentStatus status) throws SQLException {
        String sql = "UPDATE incidents SET status = ?, updated_at = NOW() WHERE id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, incidentId);
            ps.executeUpdate();
        }
    }

    public void logAction(int userId, String action, Integer incidentId) throws SQLException {
        String sql = "INSERT INTO audit_logs (user_id, action, incident_id, created_at) " +
                "VALUES (?, ?, ?, NOW())";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, action);
            if (incidentId == null) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, incidentId);
            }
            ps.executeUpdate();
        }
    }

    public List<Incident> getAllIncidentsForPriority() throws SQLException {
        return getAllIncidents();
    }

    public ReportCounts getReportCounts() throws SQLException {
        ReportCounts rc = new ReportCounts();
        String sql = "SELECT status, COUNT(*) AS cnt FROM incidents GROUP BY status";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rc.statusCounts.put(rs.getString("status"), rs.getInt("cnt"));
            }
        }

        sql = "SELECT severity, COUNT(*) AS cnt FROM incidents GROUP BY severity";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rc.severityCounts.put(rs.getString("severity"), rs.getInt("cnt"));
            }
        }

        sql = "SELECT type, COUNT(*) AS cnt FROM incidents GROUP BY type";
        try (Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rc.typeCounts.put(rs.getString("type"), rs.getInt("cnt"));
            }
        }

        return rc;
    }

    public static class ReportCounts {
        public java.util.Map<String, Integer> statusCounts = new java.util.HashMap<>();
        public java.util.Map<String, Integer> severityCounts = new java.util.HashMap<>();
        public java.util.Map<String, Integer> typeCounts = new java.util.HashMap<>();
    }

    private Incident mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String description = rs.getString("description");
        IncidentType type = IncidentType.valueOf(rs.getString("type"));
        int impact = rs.getInt("impact");
        int likelihood = rs.getInt("likelihood");
        int exposure = rs.getInt("exposure");
        int riskScore = rs.getInt("risk_score");
        String severity = rs.getString("severity");
        IncidentStatus status = IncidentStatus.valueOf(rs.getString("status"));
        int reportedBy = rs.getInt("reported_by");
        Integer assignedTo = rs.getObject("assigned_to") != null ? rs.getInt("assigned_to") : null;
        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime createdAt = ts != null ? ts.toLocalDateTime() : null;

        return new Incident(id, title, description, type,
                impact, likelihood, exposure, riskScore, severity,
                status, reportedBy, assignedTo, createdAt);
    }
}
