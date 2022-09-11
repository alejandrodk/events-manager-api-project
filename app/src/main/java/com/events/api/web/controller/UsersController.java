package com.events.api.web.controller;

import com.events.api.domain.model.User;
import com.events.api.domain.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @PostMapping("/users")
    @Operation(summary = "create a new user")
    public ResponseEntity<User> create(@RequestBody User dto) {
        User result = this.service.create(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
