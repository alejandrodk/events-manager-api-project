package com.events.api.tickets;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/tickets/{ticket}/cancel")
    public TicketsEntity cancel(@PathVariable("ticket") @NotNull int ticket) {
        return this.service.cancel(ticket);
    }
}
