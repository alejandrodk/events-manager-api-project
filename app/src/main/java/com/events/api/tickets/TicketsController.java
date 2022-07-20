package com.events.api.tickets;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class TicketsController {

    @GetMapping("/tickets/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/tickets/buy")
    public String buy() {
        return "bought";
    }

    @PostMapping("/tickets/cancel")
    public String cancel() {
        return "cancelled";
    }
}
