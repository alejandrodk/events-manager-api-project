package com.events.api.events;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventsService {
    private final EventsRepository repository;

    public EventsService(EventsRepository repository) {
        this.repository = repository;
    }

    public EventsEntity create(EventsEntity event) {
        return this.repository.save(event);
    }

    public List<EventsEntity> list() {
        return this.repository.findAll();
    }

    public EventsEntity get(int event) {
        return this.repository.findById(event).orElse(null);
    }
}
