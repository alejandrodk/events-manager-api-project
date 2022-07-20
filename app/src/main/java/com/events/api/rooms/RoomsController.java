package com.events.api.rooms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class RoomsController {

    @GetMapping("/rooms/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/rooms")
    public String create() {
        return "created";
    }

    @PutMapping("/rooms/{room}")
    public String update() {
        return "updated";
    }

    @GetMapping("/rooms")
    public String rooms() {
        return "rooms";
    }
}
