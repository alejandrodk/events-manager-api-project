package com.events.api.rooms;

import com.events.api.events.EventsEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
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

    public static boolean validateAvailability(EventsEntity event, RoomsEntity room) {
        LocalTime from = event.getFrom().toLocalTime();
        LocalTime to = event.getTo().toLocalTime();

        LocalTime open = room.getOpen().toLocalTime();
        LocalTime close = room.getClose().toLocalTime();

        return from.isAfter(open) && to.isBefore(close);
    }
}
