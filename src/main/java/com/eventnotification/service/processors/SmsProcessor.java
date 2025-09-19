package com.eventnotification.service.processors;

import com.eventnotification.model.Event;
import org.springframework.stereotype.Service;

@Service
public class SmsProcessor extends EventProcessor {

    @Override
    public void processEvent(Event event) {
        executeProcessing(event);
    }
}
