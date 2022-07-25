package com.events.api.web.controller;

import com.events.api.data.entity.UsersEntity;
import com.events.api.domain.service.UsersService;
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
    public String ping() {
        return "pong";
    }

    @PostMapping("/users")
    public UsersEntity create(@RequestBody UsersEntity dto) {
        return this.service.create(dto);
    }
}
