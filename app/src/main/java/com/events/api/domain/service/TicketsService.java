package com.events.api.domain.service;

import com.events.api.domain.model.Ticket;
import com.events.api.data.entity.UsersEntity;
import com.events.api.domain.gateway.TicketGateway;
import com.events.api.domain.model.Event;
import com.events.api.domain.model.Room;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketsService {
    private final TicketGateway ticketGateway;
    private final EventsService eventsService;
    private final UsersService usersService;

    public TicketsService(TicketGateway ticketGateway, EventsService eventsService, UsersService usersService) {
        this.ticketGateway = ticketGateway;
        this.eventsService = eventsService;
        this.usersService = usersService;
    }

    public Ticket create(Ticket ticket) {
        return this.ticketGateway.save(ticket);
    }

    public Ticket cancel(int id) {
        Ticket ticket = this.ticketGateway.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setCancelled(true);

        return this.ticketGateway.save(ticket);
    }

    public List<Ticket> findByEvent(int eventId) {
        return this.ticketGateway.findByEvent(eventId);
    }

    public void validateAvailability(Ticket ticket) {
        Event event = this.eventsService.get(ticket.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Room room = this.eventsService.getEventRoom(event.getRoom())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        UsersEntity user = this.usersService.get(ticket.getClient())
                .orElseThrow(() -> new RuntimeException("User not found"));

        long purchasedTickets = this.ticketGateway.countByEvent(event.getId());
        long currentUserTickets = this.ticketGateway.countByClient(user.getId());

        if (purchasedTickets == room.getAvailability()) {
            throw new RuntimeException("room not available");
        }

        if (currentUserTickets > 0) {
            throw new RuntimeException("User already has a ticket");
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(event.getTo())) {
            throw new RuntimeException("Event already finished");
        }
    }
}
