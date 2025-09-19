package com.eventnotification.controller;

import com.eventnotification.dto.EventRequest;
import com.eventnotification.model.Event;
import com.eventnotification.enums.EventType;
import com.eventnotification.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testValidEmailEventSubmission() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("recipient", "test@example.com");
        payload.put("message", "Welcome!");

        EventRequest request = new EventRequest("EMAIL", payload, "http://callback.url");
        Event mockEvent = new Event(EventType.EMAIL, payload, "http://callback.url");

        when(eventService.createEvent(any(EventRequest.class))).thenReturn(mockEvent);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").exists())
                .andExpect(jsonPath("$.message").value("Event accepted for processing."));
    }

    @Test
    void testValidSmsEventSubmission() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("phoneNumber", "+911234567890");
        payload.put("message", "Your OTP is 123456");

        EventRequest request = new EventRequest("SMS", payload, "http://callback.url");
        Event mockEvent = new Event(EventType.SMS, payload, "http://callback.url");

        when(eventService.createEvent(any(EventRequest.class))).thenReturn(mockEvent);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").exists())
                .andExpect(jsonPath("$.message").value("Event accepted for processing."));
    }

    @Test
    void testValidPushEventSubmission() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("deviceId", "abc-123-xyz");
        payload.put("message", "Your order has been shipped!");

        EventRequest request = new EventRequest("PUSH", payload, "http://callback.url");
        Event mockEvent = new Event(EventType.PUSH, payload, "http://callback.url");

        when(eventService.createEvent(any(EventRequest.class))).thenReturn(mockEvent);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").exists())
                .andExpect(jsonPath("$.message").value("Event accepted for processing."));
    }

    @Test
    void testInvalidEventType() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("recipient", "test@example.com");
        payload.put("message", "Welcome!");

        EventRequest request = new EventRequest("INVALID_TYPE", payload, "http://callback.url");

        when(eventService.createEvent(any(EventRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid event type: INVALID_TYPE"));

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid event type: INVALID_TYPE"));
    }

    @Test
    void testMissingEventType() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("recipient", "test@example.com");
        payload.put("message", "Welcome!");

        EventRequest request = new EventRequest(null, payload, "http://callback.url");

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testMissingPayload() throws Exception {
        EventRequest request = new EventRequest("EMAIL", null, "http://callback.url");

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testMissingCallbackUrl() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("recipient", "test@example.com");
        payload.put("message", "Welcome!");

        EventRequest request = new EventRequest("EMAIL", payload, null);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testMissingPayloadFields() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("message", "Welcome!");

        EventRequest request = new EventRequest("EMAIL", payload, "http://callback.url");

        when(eventService.createEvent(any(EventRequest.class)))
                .thenThrow(new IllegalArgumentException("Email event requires 'recipient' and 'message' fields"));

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email event requires 'recipient' and 'message' fields"));
    }
}
