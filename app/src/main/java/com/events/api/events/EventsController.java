package com.events.api.events;

import com.events.api.events.dtos.EventDTO;
import com.events.api.rooms.RoomsEntity;
import com.events.api.rooms.RoomsService;
import com.events.api.tickets.TicketsEntity;
import com.events.api.tickets.TicketsService;
import com.events.api.utils.ModelMapperUtils;
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
        List<EventsEntity> result = this.service.list(date.orElse(""));
        return result.stream().map(event -> {
            RoomsEntity room = this.roomsService.get(event.getRoom()).get();
            List<TicketsEntity> tickets = this.ticketsService.findByEvent(event.getId());

            return EventDTO.fromEntity(event, room, tickets);
        }).toList();
    }

    @PostMapping("/batch/events")
    public String batch() {
        return "batch";
    }
}
