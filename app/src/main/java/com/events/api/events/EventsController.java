package com.events.api.events;

import com.events.api.utils.ModelMapperUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController()
public class EventsController {

    private final EventsService service;

    public EventsController(EventsService service) {
        this.service = service;
    }

    @GetMapping("/events/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/events")
    public EventsEntity create(@RequestBody EventsEntity dto) {
        EventsEntity entity = ModelMapperUtils.mapToClass(dto, EventsEntity.class);
        return this.service.create(entity);
    }

    @PutMapping("/events/{event}")
    public EventsEntity update(@PathVariable("event") @NotNull int event) {
        return this.service.get(event).orElseThrow(() -> new RuntimeException("event not found"));
    }

    @GetMapping("/events")
    public List<EventsEntity> list(@RequestParam(value = "date") Optional<String> date) {
        return this.service.list(date.orElse(""));
    }

    @PostMapping("/batch/events")
    public String batch() {
        return "batch";
    }
}
