package com.events.api.web.dto;

import com.events.api.domain.model.PastEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PastEventDTO {
    private int id;
    private String name;
    private String room;
    private BigDecimal price;
    private int totalTickets;
    private int soldTickets;
    private int availableTickets;
    private String from;
    private String to;

    public static PastEventDTO fromModel(PastEvent event) {
        return PastEventDTO.builder()
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
