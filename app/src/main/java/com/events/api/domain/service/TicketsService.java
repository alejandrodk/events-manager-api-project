package com.events.api.domain.service;

import com.events.api.data.entity.TicketsEntity;
import com.events.api.data.repository.TicketsRepository;
import com.events.api.data.entity.RoomsEntity;
import com.events.api.data.entity.UsersEntity;
import com.events.api.domain.model.Event;
import com.events.api.domain.model.Room;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketsService {
    private final TicketsRepository repository;
    private final EventsService eventsService;
    private final UsersService usersService;

    public TicketsService(TicketsRepository repository, EventsService eventsService, UsersService usersService) {
        this.repository = repository;
        this.eventsService = eventsService;
        this.usersService = usersService;
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

    public List<TicketsEntity> findByEvent(int eventId) {
        return this.repository.findByEventId(eventId);
    }

    public void validateAvailability(TicketsEntity ticket) {
        Event event = this.eventsService.get(ticket.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Room room = this.eventsService.getEventRoom(event.getRoom())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        UsersEntity user = this.usersService.get(ticket.getClient())
                .orElseThrow(() -> new RuntimeException("User not found"));

        long purchasedTickets = this.repository.countByEventIdAndCancelledFalse(event.getId());
        long currentUserTickets = this.repository.countByClient(user.getId());

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
