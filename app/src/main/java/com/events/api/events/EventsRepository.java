package com.events.api.events;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventsRepository extends JpaRepository<EventsEntity, Integer> {
    List<EventsEntity> findByRoom(int room);
}
