package com.eventnotification.dto;

import java.time.LocalDateTime;

public class CallbackRequest {
    private String eventId;
    private String status;
    private String eventType;
    private String errorMessage;
    private LocalDateTime processedAt;

    public CallbackRequest() {}

    public CallbackRequest(String eventId, String status, String eventType, LocalDateTime processedAt) {
        this.eventId = eventId;
        this.status = status;
        this.eventType = eventType;
        this.processedAt = processedAt;
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
}
