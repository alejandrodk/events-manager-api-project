package com.events.api.tickets;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketsRepository extends JpaRepository<TicketsEntity, Integer> {
    List<TicketsEntity> findByEventId(int eventId);
    long countByEventIdAndCancelledFalse(int eventId);
    long countByClient(int client);
}
