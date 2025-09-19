package com.eventnotification.service;

import com.eventnotification.dto.EventRequest;
import com.eventnotification.enums.EventType;
import com.eventnotification.model.Event;
import com.eventnotification.service.processors.EmailProcessor;
import com.eventnotification.service.processors.PushProcessor;
import com.eventnotification.service.processors.SmsProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EventService {

    private static final String MESSAGE_KEY = "message";
    private static final String RECIPIENT_KEY = "recipient";
    private static final String PHONE_KEY = "phoneNumber";
    private static final String DEVICE_KEY = "deviceId";

    @Autowired
    private EmailProcessor emailProcessor;

    @Autowired
    private SmsProcessor smsProcessor;

    @Autowired
    private PushProcessor pushProcessor;

    public Event createEvent(EventRequest request) {
        // Validate event type
        EventType eventType;
        try {
            eventType = EventType.valueOf(request.getEventType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid event type: " + request.getEventType());
        }

        // Validate payload based on event type
        validatePayload(eventType, request.getPayload());

        // Create event
        Event event = new Event(eventType, request.getPayload(), request.getCallbackUrl());

        // Submit to appropriate processor
        switch (eventType) {
            case EMAIL -> emailProcessor.processEvent(event);
            case SMS -> smsProcessor.processEvent(event);
            case PUSH -> pushProcessor.processEvent(event);
        }

        return event;
    }

    private void validatePayload(EventType eventType, Map<String, Object> payload) {
        switch (eventType) {
            case EMAIL -> {
                if (!payload.containsKey(RECIPIENT_KEY) || !payload.containsKey(MESSAGE_KEY)) {
                    throw new IllegalArgumentException("Email event requires 'recipient' and 'message' fields");
                }
            }
            case SMS -> {
                if (!payload.containsKey(PHONE_KEY) || !payload.containsKey(MESSAGE_KEY)) {
                    throw new IllegalArgumentException("SMS event requires 'phoneNumber' and 'message' fields");
                }
            }
            case PUSH -> {
                if (!payload.containsKey(DEVICE_KEY) || !payload.containsKey(MESSAGE_KEY)) {
                    throw new IllegalArgumentException("Push event requires 'deviceId' and 'message' fields");
                }
            }
        }
    }
}
