package com.events.api.domain.service;

import com.events.api.data.cache.RoomsCache;
import com.events.api.domain.gateway.RoomGateway;
import com.events.api.domain.model.Event;
import com.events.api.domain.model.Room;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoomsService {
    private final RoomGateway roomGateway;
    private final RoomsCache cache;

    public RoomsService(RoomGateway roomGateway, RoomsCache cache) {
        this.roomGateway = roomGateway;
        this.cache = cache;
    }

    public Room create(Room room) {
        Room result = this.roomGateway.save(room);
        return this.cache.populate(result);
    }

    public Room update(Room room) {
        boolean exists = this.roomGateway.exist(room.getId());
        if (!exists) throw new RuntimeException("Room not found");
        Room result = this.roomGateway.save(room);

        return this.cache.populate(result);
    }

    public List<Room> list() {
        return this.roomGateway.findAll();
    }

    public Optional<Room> get(int id) {
        Room cached = this.cache.get(id);
        if (cached != null) return Optional.of(cached);
        return this.roomGateway.findById(id);
    }

    public boolean validateAvailability(Event event, Room room, List<Event> currentRoomEvents) {
        LocalTime from = event.getFrom().toLocalTime();
        LocalTime to = event.getTo().toLocalTime();

        LocalTime open = room.getOpen().toLocalTime();
        LocalTime close = room.getClose().toLocalTime();

        List<Event> currentRoomEventsWithSameDate = currentRoomEvents.stream()
                .filter(ev -> ev.getFrom().getDayOfYear() == event.getFrom().getDayOfYear())
                .filter(ev -> !event.getFrom().isAfter(ev.getTo()) || event.getFrom().equals(ev.getFrom()))
                .toList();

        if (currentRoomEventsWithSameDate.size() > 0) return false;

        return (from.isAfter(open) || from.equals(open)) && (to.isBefore(close) || to.equals(close));
    }
}
