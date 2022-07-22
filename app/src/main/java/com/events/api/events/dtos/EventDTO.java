package com.events.api.events.dtos;

import com.events.api.events.EventsEntity;
import com.events.api.rooms.RoomsEntity;
import com.events.api.tickets.TicketsEntity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class EventDTO {
    private int id;
    private String name;
    private String room;
    private BigDecimal price;
    private int totalTickets;
    private int soldTickets;
    private int availableTickets;
    private LocalDateTime from;
    private LocalDateTime to;

    public static EventDTO fromEntity(
            EventsEntity event,
            RoomsEntity room,
            List<TicketsEntity> tickets
    ) {
        int sold = tickets.stream().
                filter(ticket -> !ticket.getCancelled()).toList().size();

        return EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .room(room.getName())
                .price(event.getPrice())
                .totalTickets(room.getAvailability())
                .soldTickets(sold)
                .availableTickets(room.getAvailability() - sold)
                .from(event.getFrom())
                .to(event.getTo())
                .build();
    }
}
