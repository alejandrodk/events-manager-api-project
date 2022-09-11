package com.events.api.web.controller;

import com.events.api.domain.model.Ticket;
import com.events.api.domain.service.TicketsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController()
public class TicketsController {
    private final TicketsService service;

    public TicketsController(TicketsService service) {
        this.service = service;
    }

    @GetMapping("/tickets/ping")
    @Operation(summary = "health check endpoint")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @PostMapping("/tickets/buy")
    public ResponseEntity<Ticket> buy(@RequestBody Ticket dto) {
        this.service.validateAvailability(dto);
        Ticket result = this.service.create(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/tickets/{ticket}/cancel")
    public ResponseEntity<Ticket> cancel(@PathVariable("ticket") @NotNull int ticket) {
        Ticket result = this.service.cancel(ticket);
        return ResponseEntity.ok(result);
    }
}
