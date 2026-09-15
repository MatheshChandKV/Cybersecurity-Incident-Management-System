package com.cims;

import java.time.LocalDateTime;

public class Incident {
    private int id;
    private String title;
    private String description;
    private IncidentType type;
    private int impact;
    private int likelihood;
    private int exposure;
    private int riskScore;
    private String severity;
    private IncidentStatus status;
    private int reportedBy;
    private Integer assignedTo;
    private LocalDateTime createdAt;

    public Incident(int id, String title, String description,
            IncidentType type, int impact, int likelihood,
            int exposure, int riskScore, String severity,
            IncidentStatus status, int reportedBy,
            Integer assignedTo, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.impact = impact;
        this.likelihood = likelihood;
        this.exposure = exposure;
        this.riskScore = riskScore;
        this.severity = severity;
        this.status = status;
        this.reportedBy = reportedBy;
        this.assignedTo = assignedTo;
        this.createdAt = createdAt;
    }

    public Incident(String title, String description, IncidentType type,
            int impact, int likelihood, int exposure,
            int reportedBy) {
        this(-1, title, description, type, impact, likelihood, exposure,
                0, null, IncidentStatus.REPORTED, reportedBy,
                null, null);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public IncidentType getType() {
        return type;
    }

    public int getImpact() {
        return impact;
    }

    public int getLikelihood() {
        return likelihood;
    }

    public int getExposure() {
        return exposure;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public String getSeverity() {
        return severity;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public int getReportedBy() {
        return reportedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setRiskScore(int riskScore) {
        this.riskScore = riskScore;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public void setStatus(IncidentStatus status) {
        this.status = status;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }
}
