package com.events.api.rooms;

import org.springframework.stereotype.Service;

import java.util.List;

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
        Boolean exist = this.repository.existsById(room.getId());
        if (exist.equals(true)) {
           throw new RuntimeException("Room not found");
        }
        return this.repository.save(room);
    }

    public List<RoomsEntity> list() {
        return this.repository.findAll();
    }
}
