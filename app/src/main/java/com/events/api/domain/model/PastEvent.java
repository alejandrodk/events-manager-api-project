package com.events.api.domain.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@Document("events")
public class PastEvent {
    @Id
    private int id;
    private String name;
    private String room;
    private BigDecimal price;
    private int totalTickets;
    private int soldTickets;
    private int availableTickets;
    private String from;
    private String to;

    public static PastEvent fromEntity(
            Event event,
            Room room,
            List<Ticket> tickets
    ) {
        int sold = tickets.stream().
                filter(ticket -> !ticket.getCancelled()).toList().size();

        return PastEvent.builder()
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
}
