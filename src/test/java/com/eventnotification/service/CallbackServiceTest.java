package com.eventnotification.service;

import com.eventnotification.dto.CallbackRequest;
import com.eventnotification.enums.EventStatus;
import com.eventnotification.enums.EventType;
import com.eventnotification.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CallbackServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private CallbackService callbackService;

    @BeforeEach
    void setUp() {
        callbackService = new CallbackService();
        ReflectionTestUtils.setField(callbackService, "restTemplate", restTemplate);
    }

    @Test
    void testSuccessfulCallback() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("recipient", "test@example.com");
        payload.put("message", "Test message");

        Event event = new Event(EventType.EMAIL, payload, "http://callback.url/webhook");
        event.setStatus(EventStatus.COMPLETED);
        event.setProcessedAt(LocalDateTime.now());

        when(restTemplate.postForObject(eq("http://callback.url/webhook"), any(CallbackRequest.class), eq(String.class)))
                .thenReturn("OK");

        callbackService.sendCallback(event);

        ArgumentCaptor<CallbackRequest> callbackCaptor = ArgumentCaptor.forClass(CallbackRequest.class);
        verify(restTemplate).postForObject(eq("http://callback.url/webhook"), callbackCaptor.capture(), eq(String.class));

        CallbackRequest capturedCallback = callbackCaptor.getValue();
        assertEquals(event.getEventId(), capturedCallback.getEventId());
        assertEquals("COMPLETED", capturedCallback.getStatus());
        assertEquals("EMAIL", capturedCallback.getEventType());
        assertNotNull(capturedCallback.getProcessedAt());
        assertNull(capturedCallback.getErrorMessage());
    }

    @Test
    void testFailedCallback() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("recipient", "test@example.com");
        payload.put("message", "Test message");

        Event event = new Event(EventType.EMAIL, payload, "http://callback.url/webhook");
        event.setStatus(EventStatus.FAILED);
        event.setErrorMessage("Simulated processing failure");
        event.setProcessedAt(LocalDateTime.now());

        when(restTemplate.postForObject(eq("http://callback.url/webhook"), any(CallbackRequest.class), eq(String.class)))
                .thenReturn("OK");

        callbackService.sendCallback(event);

        ArgumentCaptor<CallbackRequest> callbackCaptor = ArgumentCaptor.forClass(CallbackRequest.class);
        verify(restTemplate).postForObject(eq("http://callback.url/webhook"), callbackCaptor.capture(), eq(String.class));

        CallbackRequest capturedCallback = callbackCaptor.getValue();
        assertEquals(event.getEventId(), capturedCallback.getEventId());
        assertEquals("FAILED", capturedCallback.getStatus());
        assertEquals("EMAIL", capturedCallback.getEventType());
        assertEquals("Simulated processing failure", capturedCallback.getErrorMessage());
        assertNotNull(capturedCallback.getProcessedAt());
    }
}
