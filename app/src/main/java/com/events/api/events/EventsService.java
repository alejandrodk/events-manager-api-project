package com.events.api.events;

import com.events.api.rooms.RoomsEntity;
import com.events.api.rooms.RoomsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventsService {
    private final EventsRepository repository;
    private final RoomsService roomsService;

    public EventsService(EventsRepository repository, RoomsService roomsService) {
        this.repository = repository;
        this.roomsService = roomsService;
    }

    public EventsEntity create(EventsEntity event) {
        RoomsEntity room = this.getEventRoom(event.getRoom())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        List<EventsEntity> currentRoomEvents = this.listByRoom(event.getRoom());

        boolean available = this.roomsService.validateAvailability(event, room, currentRoomEvents);

        if (!available) throw new RuntimeException("Room is not available");

        return this.repository.save(event);
    }

    public Optional<EventsEntity> get(int event) {
        return this.repository.findById(event);
    }

    public List<EventsEntity> list() {
        return this.repository.findAll();
    }

    public List<EventsEntity> listByRoom(int room) {
        return this.repository.findByRoom(room);
    }
    public Optional<RoomsEntity> getEventRoom(int roomId) {
        return this.roomsService.get(roomId);
    }
}
