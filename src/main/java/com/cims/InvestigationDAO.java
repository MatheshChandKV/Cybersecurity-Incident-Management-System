package com.cims;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InvestigationDAO {

    public void addInvestigation(Investigation inv) throws SQLException {
        String sql = "INSERT INTO investigations (incident_id, analyst_id, findings, actions, created_at) " +
                "VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, inv.getIncidentId());
            ps.setInt(2, inv.getAnalystId());
            ps.setString(3, inv.getFindings());
            ps.setString(4, inv.getActions());
            ps.executeUpdate();
        }
    }

    public List<Investigation> getInvestigationsByIncident(int incidentId) throws SQLException {
        List<Investigation> list = new ArrayList<>();
        String sql = "SELECT * FROM investigations WHERE incident_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, incidentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    private Investigation mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int incidentId = rs.getInt("incident_id");
        int analystId = rs.getInt("analyst_id");
        String findings = rs.getString("findings");
        String actions = rs.getString("actions");
        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime createdAt = ts != null ? ts.toLocalDateTime() : null;
        return new Investigation(id, incidentId, analystId, findings, actions, createdAt);
    }
}
