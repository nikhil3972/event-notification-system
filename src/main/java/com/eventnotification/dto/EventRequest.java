package com.eventnotification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class EventRequest {
    @NotNull(message = "Event type is required")
    private String eventType;

    @NotNull(message = "Payload is required")
    private Map<String, Object> payload;

    @NotBlank(message = "Callback URL is required")
    private String callbackUrl;

    public EventRequest() {}

    public EventRequest(String eventType, Map<String, Object> payload, String callbackUrl) {
        this.eventType = eventType;
        this.payload = payload;
        this.callbackUrl = callbackUrl;
    }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public Map<String, Object> getPayload() { return payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }

    public String getCallbackUrl() { return callbackUrl; }
    public void setCallbackUrl(String callbackUrl) { this.callbackUrl = callbackUrl; }
}
