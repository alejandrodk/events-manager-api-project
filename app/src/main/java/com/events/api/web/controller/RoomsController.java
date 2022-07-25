package com.events.api.web.controller;

import com.events.api.domain.model.Room;
import com.events.api.domain.service.RoomsService;
import com.events.api.domain.utils.ModelMapperUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
public class RoomsController {

    private final RoomsService service;

    public RoomsController(RoomsService service) {
        this.service = service;
    }

    @GetMapping("/rooms/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/rooms")
    public Room create(@RequestBody Room dto) {
        Room room = ModelMapperUtils.mapToClass(dto, Room.class);
        return this.service.create(room);
    }

    @PutMapping("/rooms")
    public Room update(@RequestBody Room dto) {
        Room room = ModelMapperUtils.mapToClass(dto, Room.class);
        return this.service.update(room);
    }

    @GetMapping("/rooms")
    public List<Room> rooms() {
        return this.service.list();
    }

    @GetMapping("/rooms/{room}")
    public Room room(@PathVariable("room") int room) {
        return this.service.get(room).get();
    }
}
