package com.events.api.events;

import org.springframework.stereotype.Service;

@Service
public class EventsService {
    private final EventsRepository repository;

    public EventsService(EventsRepository repository) {
        this.repository = repository;
    }

    public EventsEntity create(EventsEntity event) {
        return this.repository.save(event);
    }
}
