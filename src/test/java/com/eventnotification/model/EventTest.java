package com.eventnotification.model;

import com.eventnotification.enums.EventStatus;
import com.eventnotification.enums.EventType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void testEventCreation() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("recipient", "test@example.com");
        payload.put("message", "Test message");

        Event event = new Event(EventType.EMAIL, payload, "http://callback.url");

        assertNotNull(event.getEventId());
        assertTrue(event.getEventId().startsWith("e"));
        assertEquals(EventType.EMAIL, event.getEventType());
        assertEquals(payload, event.getPayload());
        assertEquals("http://callback.url", event.getCallbackUrl());
        assertEquals(EventStatus.PENDING, event.getStatus());
        assertNotNull(event.getCreatedAt());
        assertNull(event.getProcessedAt());
        assertNull(event.getErrorMessage());
    }

    @Test
    void testDefaultConstructor() {
        Event event = new Event();

        assertNotNull(event.getEventId());
        assertTrue(event.getEventId().startsWith("e"));
        assertEquals(EventStatus.PENDING, event.getStatus());
        assertNotNull(event.getCreatedAt());
    }
}
