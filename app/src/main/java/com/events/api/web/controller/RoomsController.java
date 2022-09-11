package com.events.api.web.controller;

import com.events.api.domain.model.Room;
import com.events.api.domain.service.RoomsService;
import com.events.api.domain.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @PostMapping("/rooms")
    @Operation(summary = "create a new room")
    public ResponseEntity<Room> create(@RequestBody Room dto) {
        Room room = ModelMapperUtils.mapToClass(dto, Room.class);
        Room result = this.service.create(room);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/rooms")
    @Operation(summary = "update room if exists")
    public ResponseEntity<Room> update(@RequestBody Room dto) {
        Room room = ModelMapperUtils.mapToClass(dto, Room.class);
        Room result = this.service.update(room);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/rooms")
    @Operation(summary = "get a list of rooms")
    public ResponseEntity<List<Room>> rooms() {
        List<Room> result = this.service.list();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/rooms/{room}")
    @Operation(summary = "get a single room by id")
    public ResponseEntity<Room> room(@PathVariable("room") int room) {
        Room result = this.service.get(room).orElseThrow(() -> new RuntimeException("room not found"));
        return ResponseEntity.ok(result);
    }
}
