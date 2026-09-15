package com.cims;

import java.time.LocalDateTime;

public class Investigation {
    private int id;
    private int incidentId;
    private int analystId;
    private String findings;
    private String actions;
    private LocalDateTime createdAt;

    public Investigation(int incidentId, int analystId,
                          String findings, String actions) {
        this(-1, incidentId, analystId, findings, actions, null);
    }

    public Investigation(int id, int incidentId, int analystId,
                         String findings, String actions,
                         LocalDateTime createdAt) {
        this.id = id;
        this.incidentId = incidentId;
        this.analystId = analystId;
        this.findings = findings;
        this.actions = actions;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getIncidentId() { return incidentId; }
    public int getAnalystId() { return analystId; }
    public String getFindings() { return findings; }
    public String getActions() { return actions; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
