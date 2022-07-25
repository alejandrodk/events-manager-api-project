package com.events.api.domain.model;

import lombok.*;

@Builder
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private Integer id;
    private int eventId;
    private int client;
    private Boolean cancelled = false;
}
