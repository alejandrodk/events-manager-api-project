package com.events.api.tickets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@AllArgsConstructor
public class TicketsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "event_name")
    private String eventName;
    @Column(name = "event_id")
    private String eventId;
    @Column
    private String client;
    @Column
    private Boolean cancelled;
}
