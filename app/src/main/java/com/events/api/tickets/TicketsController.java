package com.events.api.tickets;

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
    public TicketsEntity buy(@RequestBody TicketsEntity dto) {
        this.service.validateAvailability(dto);
        return this.service.create(dto);
    }

    @PostMapping("/tickets/{ticket}/cancel")
    public TicketsEntity cancel(@PathVariable("ticket") @NotNull int ticket) {
        return this.service.cancel(ticket);
    }
}
