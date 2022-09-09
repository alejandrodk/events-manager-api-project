package com.events.api.web.controller;

import com.events.api.data.entity.EventsEntity;
import com.events.api.domain.model.Event;
import com.events.api.domain.model.PastEvent;
import com.events.api.domain.model.Room;
import com.events.api.domain.model.Ticket;
import com.events.api.domain.service.EventsService;
import com.events.api.web.dto.EventDTO;
import com.events.api.web.dto.PastEventDTO;
import com.events.api.domain.service.RoomsService;
import com.events.api.domain.service.TicketsService;
import com.events.api.domain.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController()
public class EventsController {
    private final EventsService service;
    private final TicketsService ticketsService;
    private final RoomsService roomsService;


    public EventsController(EventsService service, TicketsService ticketsService, RoomsService roomsService) {
        this.service = service;
        this.ticketsService = ticketsService;
        this.roomsService = roomsService;
    }

    @GetMapping("/events/ping")
    @Operation(summary = "health check endpoint")
    public String ping() {
        return "pong";
    }

    @PostMapping("/events")
    @Operation(summary = "create a new event")
    public Event create(@RequestBody EventsEntity dto) {
        Event entity = ModelMapperUtils.mapToClass(dto, Event.class);
        return this.service.create(entity);
    }

    @GetMapping("/events/{event}")
    @Operation(summary = "get a single event by id")
    public Event update(@PathVariable("event") @NotNull int event) {
        return this.service.get(event).orElseThrow(() -> new RuntimeException("event not found"));
    }

    @GetMapping("/events")
    @Operation(summary = "get a list of events")
    public List<EventDTO> list(@RequestParam(value = "date") Optional<String> date) {
        if (date.isPresent() && date.get().equals("past")) {
            return this.service.getPastEvents().stream().map(EventDTO::fromPastEvent).toList();
        }
        List<Event> result = this.service.list(date.orElse(""));
        return result.stream().map(event -> {
            Room room = this.roomsService.get(event.getRoom()).get();
            List<Ticket> tickets = this.ticketsService.findByEvent(event.getId());

            return EventDTO.fromEntity(event, room, tickets);
        }).toList();
    }

    @PostMapping("/batch/events")
    @Operation(summary = "migrate past events to mongodb")
    public List<PastEventDTO> batch() {
        List<Event> events = this.service.list("past");
        List<PastEvent> pastEvents = events.stream().map(event -> {
            Room room = this.roomsService.get(event.getRoom()).get();
            List<Ticket> tickets = this.ticketsService.findByEvent(event.getId());
            return PastEvent.fromEntity(event, room, tickets);
        }).toList();

        return this.service.savePastEvents(pastEvents).stream().map(PastEventDTO::fromModel).toList();
    }
}
