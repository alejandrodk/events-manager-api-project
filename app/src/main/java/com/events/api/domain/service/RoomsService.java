package com.events.api.domain.service;

import com.events.api.data.cache.RoomsCache;
import com.events.api.data.entity.EventsEntity;
import com.events.api.data.entity.RoomsEntity;
import com.events.api.data.repository.RoomsRepository;
import com.events.api.domain.model.Event;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoomsService {
    private final RoomsRepository repository;
    private final RoomsCache cache;

    public RoomsService(RoomsRepository repository, RoomsCache cache) {
        this.repository = repository;
        this.cache = cache;
    }

    public RoomsEntity create(RoomsEntity room) {
        RoomsEntity result = this.repository.save(room);
        return this.cache.populate(result);
    }

    public RoomsEntity update(RoomsEntity room) {
        boolean exists = this.repository.existsById(room.getId());
        if (!exists) throw new RuntimeException("Room not found");
        RoomsEntity result = this.repository.save(room);

        return this.cache.populate(result);
    }

    public List<RoomsEntity> list() {
        return this.repository.findAll();
    }

    public Optional<RoomsEntity> get(int id) {
        RoomsEntity cached = this.cache.get(id);
        if (cached != null) return Optional.of(cached);
        return this.repository.findById(id);
    }

    public boolean validateAvailability(Event event, RoomsEntity room, List<Event> currentRoomEvents) {
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
