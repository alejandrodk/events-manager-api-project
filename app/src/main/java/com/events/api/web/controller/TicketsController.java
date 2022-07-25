package com.events.api.web.controller;

import com.events.api.domain.model.Ticket;
import com.events.api.domain.service.TicketsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController()
public class TicketsController {
    private final TicketsService service;

    public TicketsController(TicketsService service) {
        this.service = service;
    }

    @GetMapping("/tickets/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/tickets/buy")
    public Ticket buy(@RequestBody Ticket dto) {
        this.service.validateAvailability(dto);
        return this.service.create(dto);
    }

    @PostMapping("/tickets/{ticket}/cancel")
    public Ticket cancel(@PathVariable("ticket") @NotNull int ticket) {
        return this.service.cancel(ticket);
    }
}
