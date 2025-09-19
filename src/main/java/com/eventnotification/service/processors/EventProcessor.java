package com.eventnotification.service.processors;

import com.eventnotification.enums.EventStatus;
import com.eventnotification.exception.EventProcessingException;
import com.eventnotification.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Random;

public abstract class EventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(EventProcessor.class);
    private static final Random random = new Random();
    private static final double FAILURE_RATE = 0.1; // 10% failure rate

    public abstract void processEvent(Event event);

    protected void executeProcessing(Event event) {
        try {
            logger.info("Processing event: {} of type: {}", event.getEventId(), event.getEventType());

            event.setStatus(EventStatus.PROCESSING);

            // Simulate processing time
            Thread.sleep(event.getEventType().getProcessingTimeSeconds() * 1000L);

            // Simulate random failures
            if (random.nextDouble() < FAILURE_RATE) {
                throw new EventProcessingException("Simulated processing failure");
            }

            // Mark as completed
            event.setStatus(EventStatus.COMPLETED);
            event.setProcessedAt(LocalDateTime.now());

            logger.info("Event {} processed successfully", event.getEventId());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            event.setStatus(EventStatus.FAILED);
            event.setErrorMessage("Processing interrupted");
            event.setProcessedAt(LocalDateTime.now());
            logger.error("Event {} processing interrupted", event.getEventId());
        } catch (Exception e) {
            event.setStatus(EventStatus.FAILED);
            event.setErrorMessage(e.getMessage());
            event.setProcessedAt(LocalDateTime.now());
            logger.error("Event {} processing failed: {}", event.getEventId(), e.getMessage());
        }
    }
}
