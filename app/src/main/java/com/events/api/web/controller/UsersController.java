package com.events.api.web.controller;

import com.events.api.domain.model.User;
import com.events.api.domain.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class UsersController {

    private final UsersService service;

    public UsersController(UsersService service) {
        this.service = service;
    }

    @GetMapping("/users/ping")
    @Operation(summary = "health check endpoint")
    public String ping() {
        return "pong";
    }

    @PostMapping("/users")
    @Operation(summary = "create a new user")
    public User create(@RequestBody User dto) {
        return this.service.create(dto);
    }
}
