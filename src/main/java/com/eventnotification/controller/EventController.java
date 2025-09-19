package com.eventnotification.controller;

import com.eventnotification.dto.EventRequest;
import com.eventnotification.dto.EventResponse;
import com.eventnotification.model.Event;
import com.eventnotification.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")

public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/events")
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request) {
        Event event = eventService.createEvent(request);
        EventResponse response = new EventResponse(event.getEventId(), "Event accepted for processing.");
        return ResponseEntity.ok(response);
    }
}
