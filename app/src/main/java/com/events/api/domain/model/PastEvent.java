package com.events.api.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document("events")
public class PastEvent {
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
}
