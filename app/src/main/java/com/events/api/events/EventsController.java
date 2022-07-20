package com.events.api.events;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class EventsController {

    @GetMapping("/events/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/events")
    public String create() {
        return "created";
    }

    @PutMapping("/events/{event}")
    public String update() {
        return "updated";
    }

    @GetMapping("/events")
    public String list() {
        return "list";
    }

    @PostMapping("/batch/events")
    public String batch() {
        return "batch";
    }
}
