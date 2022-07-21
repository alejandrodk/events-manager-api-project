package com.events.api.rooms;

import com.events.api.utils.ModelMapperUtils;
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
    public RoomsEntity create(@RequestBody RoomsEntity dto) {
        RoomsEntity room = ModelMapperUtils.mapToClass(dto, RoomsEntity.class);
        return this.service.create(room);
    }

    @PutMapping("/rooms/{room}")
    public RoomsEntity update(@RequestBody RoomsEntity dto) {
        RoomsEntity room = ModelMapperUtils.mapToClass(dto, RoomsEntity.class);
        return this.service.update(room);
    }

    @GetMapping("/rooms")
    public List<RoomsEntity> rooms() {
        return this.service.list();
    }
}
