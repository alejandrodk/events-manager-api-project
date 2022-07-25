package com.events.api.web.dto;

import com.events.api.data.entity.RoomsEntity;
import com.events.api.data.entity.TicketsEntity;
import com.events.api.domain.model.Event;
import com.events.api.domain.model.PastEvent;
import com.events.api.domain.model.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    private int id;
    private String name;
    private String room;
    private BigDecimal price;
    private int totalTickets;
    private int soldTickets;
    private int availableTickets;
    private String from;
    private String to;

    public static EventDTO fromEntity(
            Event event,
            Room room,
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
                .from(event.getFrom().toString())
                .to(event.getTo().toString())
                .build();
    }

    public static EventDTO fromPastEvent(PastEvent event) {
        return EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .room(event.getRoom())
                .price(event.getPrice())
                .totalTickets(event.getTotalTickets())
                .soldTickets(event.getSoldTickets())
                .availableTickets(event.getAvailableTickets())
                .from(event.getFrom())
                .to(event.getTo())
                .build();
    }
}
