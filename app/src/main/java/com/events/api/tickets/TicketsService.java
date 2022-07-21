package com.events.api.tickets;

import org.springframework.stereotype.Service;

@Service
public class TicketsService {
    private final TicketsRepository repository;

    public TicketsService(TicketsRepository repository) {
        this.repository = repository;
    }

    public TicketsEntity create(TicketsEntity ticket) {
        return this.repository.save(ticket);
    }

    public TicketsEntity cancel(int id) {
        TicketsEntity ticket = this.repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setCancelled(true);

        return this.repository.save(ticket);
    }
}
