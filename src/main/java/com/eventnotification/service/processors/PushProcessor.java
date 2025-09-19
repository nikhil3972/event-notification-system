package com.eventnotification.service.processors;

import com.eventnotification.model.Event;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PushProcessor extends EventProcessor {

    @Async("pushExecutor")
    @Override
    public void processEvent(Event event) {
        executeProcessing(event);
    }
}
