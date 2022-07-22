package com.events.api.rooms;

import com.events.api.events.EventsEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoomsService {
    private final RoomsRepository repository;

    public RoomsService(RoomsRepository repository) {
        this.repository = repository;
    }

    public RoomsEntity create(RoomsEntity room) {
        return this.repository.save(room);
    }

    public RoomsEntity update(RoomsEntity room) {
        boolean exists = this.repository.existsById(room.getId());
        if (!exists) throw new RuntimeException("Room not found");

        return this.repository.save(room);
    }

    public List<RoomsEntity> list() {
        return this.repository.findAll();
    }

    public Optional<RoomsEntity> get(int id) {
        return this.repository.findById(id);
    }

    public boolean validateAvailability(EventsEntity event, RoomsEntity room, List<EventsEntity> currentRoomEvents) {
        LocalTime from = event.getFrom().toLocalTime();
        LocalTime to = event.getTo().toLocalTime();

        LocalTime open = room.getOpen().toLocalTime();
        LocalTime close = room.getClose().toLocalTime();

        List<EventsEntity> currentRoomEventsWithSameDate = currentRoomEvents.stream()
                .filter(ev ->
                        ev.getFrom().getYear() == event.getFrom().getYear() &&
                        ev.getFrom().getMonthValue() == event.getFrom().getMonthValue() &&
                        ev.getFrom().getDayOfMonth() == event.getFrom().getDayOfMonth()
                )
                .filter(ev ->
                        !event.getFrom().isAfter(ev.getTo()) ||
                        event.getFrom().equals(ev.getFrom())
                ).toList();

        if (currentRoomEventsWithSameDate.size() > 0) return false;

        return (from.isAfter(open) || from.equals(open)) && (to.isBefore(close) || to.equals(close));
    }
}
