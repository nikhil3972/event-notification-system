package com.eventnotification.service.processors;

import com.eventnotification.model.Event;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SmsProcessor extends EventProcessor {

    @Async("smsExecutor")
    @Override
    public void processEvent(Event event) {
        executeProcessing(event);
    }
}
