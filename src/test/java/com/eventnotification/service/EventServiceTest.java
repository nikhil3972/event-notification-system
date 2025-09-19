package com.eventnotification.service;

import com.eventnotification.dto.EventRequest;
import com.eventnotification.enums.EventType;
import com.eventnotification.model.Event;
import com.eventnotification.service.processors.EmailProcessor;
import com.eventnotification.service.processors.PushProcessor;
import com.eventnotification.service.processors.SmsProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EmailProcessor emailProcessor;

    @Mock
    private SmsProcessor smsProcessor;

    @Mock
    private PushProcessor pushProcessor;

    @InjectMocks
    private EventService eventService;

    @Test
    void testCreateEmailEvent() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("recipient", "test@example.com");
        payload.put("message", "Welcome!");

        EventRequest request = new EventRequest("EMAIL", payload, "http://callback.url");

        Event event = eventService.createEvent(request);

        assertNotNull(event);
        assertEquals(EventType.EMAIL, event.getEventType());
        assertEquals(payload, event.getPayload());
        assertEquals("http://callback.url", event.getCallbackUrl());
        assertNotNull(event.getEventId());
        verify(emailProcessor).processEvent(any(Event.class));
    }

    @Test
    void testCreateSmsEvent() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("phoneNumber", "+911234567890");
        payload.put("message", "Your OTP is 123456");

        EventRequest request = new EventRequest("SMS", payload, "http://callback.url");

        Event event = eventService.createEvent(request);

        assertNotNull(event);
        assertEquals(EventType.SMS, event.getEventType());
        assertEquals(payload, event.getPayload());
        assertEquals("http://callback.url", event.getCallbackUrl());
        assertNotNull(event.getEventId());
        verify(smsProcessor).processEvent(any(Event.class));
    }

    @Test
    void testCreatePushEvent() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("deviceId", "abc-123-xyz");
        payload.put("message", "Your order has been shipped!");

        EventRequest request = new EventRequest("PUSH", payload, "http://callback.url");

        Event event = eventService.createEvent(request);

        assertNotNull(event);
        assertEquals(EventType.PUSH, event.getEventType());
        assertEquals(payload, event.getPayload());
        assertEquals("http://callback.url", event.getCallbackUrl());
        assertNotNull(event.getEventId());
        verify(pushProcessor).processEvent(any(Event.class));
    }

    @Test
    void testInvalidEventType() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("recipient", "test@example.com");
        payload.put("message", "Welcome!");

        EventRequest request = new EventRequest("INVALID", payload, "http://callback.url");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> eventService.createEvent(request));
        assertEquals("Invalid event type: INVALID", exception.getMessage());
    }

    @Test
    void testEmailEventMissingRecipient() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("message", "Welcome!");

        EventRequest request = new EventRequest("EMAIL", payload, "http://callback.url");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> eventService.createEvent(request));
        assertEquals("Email event requires 'recipient' and 'message' fields", exception.getMessage());
    }

    @Test
    void testSmsEventMissingPhoneNumber() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("message", "Your OTP is 123456");

        EventRequest request = new EventRequest("SMS", payload, "http://callback.url");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> eventService.createEvent(request));
        assertEquals("SMS event requires 'phoneNumber' and 'message' fields", exception.getMessage());
    }

    @Test
    void testPushEventMissingDeviceId() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("message", "Your order has been shipped!");

        EventRequest request = new EventRequest("PUSH", payload, "http://callback.url");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> eventService.createEvent(request));
        assertEquals("Push event requires 'deviceId' and 'message' fields", exception.getMessage());
    }
}
