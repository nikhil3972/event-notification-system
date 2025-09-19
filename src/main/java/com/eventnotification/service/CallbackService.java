package com.eventnotification.service;

import com.eventnotification.dto.CallbackRequest;
import com.eventnotification.enums.EventStatus;
import com.eventnotification.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class CallbackService {

    private static final Logger logger = LoggerFactory.getLogger(CallbackService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public void sendCallback(Event event) {
        try {
            CallbackRequest callbackRequest = new CallbackRequest(
                    event.getEventId(),
                    event.getStatus().name(),
                    event.getEventType().name(),
                    LocalDateTime.now()
            );

            if (event.getStatus() == EventStatus.FAILED) {
                callbackRequest.setErrorMessage(event.getErrorMessage());
            }

            restTemplate.postForObject(event.getCallbackUrl(), callbackRequest, String.class);
            logger.info("Callback sent successfully for event: {}", event.getEventId());
        } catch (Exception e) {
            logger.error("Failed to send callback for event: {} - Error: {}", event.getEventId(), e.getMessage());
        }
    }
}
