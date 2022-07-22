package com.events.api.tickets;

import com.events.api.events.EventsEntity;
import com.events.api.events.EventsService;
import com.events.api.rooms.RoomsEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketsService {
    private final TicketsRepository repository;
    private final EventsService eventsService;

    public TicketsService(TicketsRepository repository, EventsService eventsService) {
        this.repository = repository;
        this.eventsService = eventsService;
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
        EventsEntity event = this.eventsService.get(ticket.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        RoomsEntity room = this.eventsService.getEventRoom(event.getRoom())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        long currentTickets = this.repository.countByEventIdAndCancelledFalse(event.getId());

        if (currentTickets == room.getAvailability()) {
            throw new RuntimeException("room not available");
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(event.getTo())) {
            throw new RuntimeException("Event already finished");
        }
    }
}
