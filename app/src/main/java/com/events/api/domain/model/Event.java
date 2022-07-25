package com.events.api.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@EqualsAndHashCode
public class Event {
    private Integer id;
    private String name;
    private Integer room;
    private BigDecimal price;
    private LocalDateTime from;
    private LocalDateTime to;
}
