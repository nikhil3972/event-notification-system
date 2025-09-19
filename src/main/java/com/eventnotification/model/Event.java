package com.eventnotification.model;

import com.eventnotification.enums.EventStatus;
import com.eventnotification.enums.EventType;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class Event {
    private String eventId;
    private EventType eventType;
    private Map<String, Object> payload;
    private String callbackUrl;
    private EventStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private String errorMessage;

    public Event() {
        this.eventId = "e" + UUID.randomUUID().toString().substring(0, 8);
        this.createdAt = LocalDateTime.now();
        this.status = EventStatus.PENDING;
    }

    public Event(EventType eventType, Map<String, Object> payload, String callbackUrl) {
        this();
        this.eventType = eventType;
        this.payload = payload;
        this.callbackUrl = callbackUrl;
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }

    public Map<String, Object> getPayload() { return payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }

    public String getCallbackUrl() { return callbackUrl; }
    public void setCallbackUrl(String callbackUrl) { this.callbackUrl = callbackUrl; }

    public EventStatus getStatus() { return status; }
    public void setStatus(EventStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
