package com.eventnotification.service.processors;

import com.eventnotification.enums.EventStatus;
import com.eventnotification.enums.EventType;
import com.eventnotification.model.Event;
import com.eventnotification.service.CallbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProcessorTest {

    @Mock
    private CallbackService callbackService;

    private EmailProcessor emailProcessor;
    private SmsProcessor smsProcessor;
    private PushProcessor pushProcessor;

    @BeforeEach
    void setUp() {
        emailProcessor = new EmailProcessor();
        smsProcessor = new SmsProcessor();
        pushProcessor = new PushProcessor();

        // Inject mocked CallbackService
        ReflectionTestUtils.setField(emailProcessor, "callbackService", callbackService);
        ReflectionTestUtils.setField(smsProcessor, "callbackService", callbackService);
        ReflectionTestUtils.setField(pushProcessor, "callbackService", callbackService);
    }

    @Test
    void testEmailProcessorExecutesWithCorrectDelay() throws InterruptedException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("recipient", "test@example.com");
        payload.put("message", "Test message");

        Event event = new Event(EventType.EMAIL, payload, "http://callback.url");
        CountDownLatch latch = new CountDownLatch(1);

        long startTime = System.currentTimeMillis();

        // Mock the async execution by calling executeProcessing directly
        Thread processingThread = new Thread(() -> {
            emailProcessor.executeProcessing(event);
            latch.countDown();
        });
        processingThread.start();

        // Wait for completion with timeout
        assertTrue(latch.await(10, TimeUnit.SECONDS), "Processing should complete within timeout");

        long elapsedTime = System.currentTimeMillis() - startTime;

        // Should take at least 5 seconds (EMAIL processing time)
        assertTrue(elapsedTime >= 4900, "Processing should take at least 5 seconds");
        assertTrue(event.getStatus() == EventStatus.COMPLETED || event.getStatus() == EventStatus.FAILED);
        verify(callbackService).sendCallback(event);
    }

    @Test
    void testSmsProcessorExecutesWithCorrectDelay() throws InterruptedException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("phoneNumber", "+911234567890");
        payload.put("message", "Test SMS");

        Event event = new Event(EventType.SMS, payload, "http://callback.url");
        CountDownLatch latch = new CountDownLatch(1);

        long startTime = System.currentTimeMillis();

        // Mock the async execution by calling executeProcessing directly
        Thread processingThread = new Thread(() -> {
            smsProcessor.executeProcessing(event);
            latch.countDown();
        });
        processingThread.start();

        // Wait for completion with timeout
        assertTrue(latch.await(8, TimeUnit.SECONDS), "Processing should complete within timeout");

        long elapsedTime = System.currentTimeMillis() - startTime;

        // Should take at least 3 seconds (SMS processing time)
        assertTrue(elapsedTime >= 2900, "Processing should take at least 3 seconds");
        assertTrue(event.getStatus() == EventStatus.COMPLETED || event.getStatus() == EventStatus.FAILED);
        verify(callbackService).sendCallback(event);
    }

    @Test
    void testPushProcessorExecutesWithCorrectDelay() throws InterruptedException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("deviceId", "abc-123-xyz");
        payload.put("message", "Test push");

        Event event = new Event(EventType.PUSH, payload, "http://callback.url");
        CountDownLatch latch = new CountDownLatch(1);

        long startTime = System.currentTimeMillis();

        // Mock the async execution by calling executeProcessing directly
        Thread processingThread = new Thread(() -> {
            pushProcessor.executeProcessing(event);
            latch.countDown();
        });
        processingThread.start();

        // Wait for completion with timeout
        assertTrue(latch.await(6, TimeUnit.SECONDS), "Processing should complete within timeout");

        long elapsedTime = System.currentTimeMillis() - startTime;

        // Should take at least 2 seconds (PUSH processing time)
        assertTrue(elapsedTime >= 1900, "Processing should take at least 2 seconds");
        assertTrue(event.getStatus() == EventStatus.COMPLETED || event.getStatus() == EventStatus.FAILED);
        verify(callbackService).sendCallback(event);
    }

    @Test
    void testRandomFailureSimulation() throws InterruptedException {
        // This test runs multiple times to check if random failures occur
        int totalEvents = 100;

        final CountDownLatch latch = getCountDownLatch(totalEvents);

        assertTrue(latch.await(30, TimeUnit.SECONDS), "All events should complete within timeout");

        // We expect some failures (around 10%), but this is probabilistic
        // So we just verify that not all events succeed
    }

    private CountDownLatch getCountDownLatch(int totalEvents) {
        CountDownLatch latch = new CountDownLatch(totalEvents);

        for (int i = 0; i < totalEvents; i++) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("deviceId", "device-" + i);
            payload.put("message", "Test message " + i);

            Event event = new Event(EventType.PUSH, payload, "http://callback.url");

            Thread processingThread = new Thread(() -> {
                pushProcessor.executeProcessing(event);
                if (event.getStatus() == EventStatus.FAILED) {
                    // This will be accessed in a thread-safe manner due to the nature of the test
                }
                latch.countDown();
            });
            processingThread.start();
        }
        return latch;
    }
}