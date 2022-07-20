package com.events.api.users;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class UsersController {

    @GetMapping("/users/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/users")
    public String create() {
        return "created";
    }
}
