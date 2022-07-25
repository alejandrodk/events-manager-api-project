package com.events.api.events.dtos;

import com.events.api.events.EventsEntity;
import com.events.api.rooms.RoomsEntity;
import com.events.api.tickets.TicketsEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document("events")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PastEventDTO {
    @Id
    private String _id;
    private int id;
    private String name;
    private String room;
    private BigDecimal price;
    private int totalTickets;
    private int soldTickets;
    private int availableTickets;
    private String from;
    private String to;

    public static PastEventDTO fromEntity(
            EventsEntity event,
            RoomsEntity room,
            List<TicketsEntity> tickets
    ) {
        int sold = tickets.stream().
                filter(ticket -> !ticket.getCancelled()).toList().size();

        return PastEventDTO.builder()
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
