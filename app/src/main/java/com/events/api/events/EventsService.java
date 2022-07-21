package com.events.api.events;

import com.events.api.rooms.RoomsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventsService {
    private final EventsRepository repository;
    private final RoomsRepository roomsRepository;

    public EventsService(EventsRepository repository, RoomsRepository roomsRepository) {
        this.repository = repository;
        this.roomsRepository = roomsRepository;
    }

    public EventsEntity create(EventsEntity event) {
        boolean existsRoom = roomsRepository.existsById(event.getRoom());

        if (!existsRoom) throw new RuntimeException("Room not found");

        return this.repository.save(event);
    }

    public List<EventsEntity> list() {
        return this.repository.findAll();
    }

    public EventsEntity get(int event) {
        return this.repository.findById(event).orElse(null);
    }
}
