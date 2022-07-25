package com.events.api.web.controller;

import com.events.api.data.entity.EventsEntity;
import com.events.api.domain.service.EventsService;
import com.events.api.web.dto.EventDTO;
import com.events.api.web.dto.PastEventDTO;
import com.events.api.data.entity.RoomsEntity;
import com.events.api.domain.service.RoomsService;
import com.events.api.data.entity.TicketsEntity;
import com.events.api.domain.service.TicketsService;
import com.events.api.domain.utils.ModelMapperUtils;
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
    public List<EventDTO> list(@RequestParam(value = "date") Optional<String> date) {
        if (date.isPresent() && date.get().equals("past")) {
            return this.service.getPastEvents().stream().map(EventDTO::fromPastEvent).toList();
        }
        List<EventsEntity> result = this.service.list(date.orElse(""));
        return result.stream().map(event -> {
            RoomsEntity room = this.roomsService.get(event.getRoom()).get();
            List<TicketsEntity> tickets = this.ticketsService.findByEvent(event.getId());

            return EventDTO.fromEntity(event, room, tickets);
        }).toList();
    }

    @PostMapping("/batch/events")
    public List<PastEventDTO> batch() {
        List<EventsEntity> events = this.service.list("past");
        List<PastEventDTO> pastEvents = events.stream().map(event -> {
            RoomsEntity room = this.roomsService.get(event.getRoom()).get();
            List<TicketsEntity> tickets = this.ticketsService.findByEvent(event.getId());
            return PastEventDTO.fromEntity(event, room, tickets);
        }).toList();

        return this.service.savePastEvents(pastEvents);
    }
}
