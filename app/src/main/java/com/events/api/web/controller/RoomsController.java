package com.events.api.web.controller;

import com.events.api.domain.model.Room;
import com.events.api.domain.service.RoomsService;
import com.events.api.domain.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
public class RoomsController {

    private final RoomsService service;

    public RoomsController(RoomsService service) {
        this.service = service;
    }

    @GetMapping("/rooms/ping")
    @Operation(summary = "health check endpoint")
    public String ping() {
        return "pong";
    }

    @PostMapping("/rooms")
    @Operation(summary = "create a new room")
    public Room create(@RequestBody Room dto) {
        Room room = ModelMapperUtils.mapToClass(dto, Room.class);
        return this.service.create(room);
    }

    @PutMapping("/rooms")
    @Operation(summary = "update room if exists")
    public Room update(@RequestBody Room dto) {
        Room room = ModelMapperUtils.mapToClass(dto, Room.class);
        return this.service.update(room);
    }

    @GetMapping("/rooms")
    @Operation(summary = "get a list of rooms")
    public List<Room> rooms() {
        return this.service.list();
    }

    @GetMapping("/rooms/{room}")
    @Operation(summary = "get a single room by id")
    public Room room(@PathVariable("room") int room) {
        return this.service.get(room).get();
    }
}
