package com.events.api.domain.service;

import com.events.api.domain.gateway.UserGateway;
import com.events.api.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {
    private final UserGateway userGateway;

    public UsersService(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User create(User user) {
        Optional<User> exists = this.userGateway.findByDniOrEmail(user.getDni(), user.getEmail());

        if (exists.isPresent()) throw new RuntimeException("user already exists");

        return this.userGateway.save(user);
    }

    public Optional<User> get(int id) {
        return this.userGateway.findById(id);
    }
}
