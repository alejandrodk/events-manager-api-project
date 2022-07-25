package com.events.api.data.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tickets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "event_id")
    private int eventId;
    @Column
    private int client;
    @Column
    private Boolean cancelled = false;
}
