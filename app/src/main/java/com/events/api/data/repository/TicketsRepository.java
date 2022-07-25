package com.events.api.data.repository;

import com.events.api.data.entity.TicketsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketsRepository extends JpaRepository<TicketsEntity, Integer> {
    List<TicketsEntity> findByEventId(int eventId);
    long countByEventIdAndCancelledFalse(int eventId);
    long countByClient(int client);
}
