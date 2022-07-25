package com.events.api.data.repository;

import com.events.api.data.entity.EventsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsRepository extends JpaRepository<EventsEntity, Integer> {
    List<EventsEntity> findByRoom(int room);
    @Query(value = "SELECT t FROM EventsEntity t WHERE t.from BETWEEN :from AND :to")
    List<EventsEntity> findBetweenDates(LocalDateTime from, LocalDateTime to);
}
