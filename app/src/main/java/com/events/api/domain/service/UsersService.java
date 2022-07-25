package com.events.api.domain.service;

import com.events.api.data.entity.UsersEntity;
import com.events.api.data.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepository repository;

    public UsersService(UsersRepository repository) {
        this.repository = repository;
    }

    public UsersEntity create(UsersEntity user) {
        Optional<UsersEntity> exists = this.repository.findOneByDniOrEmail(user.getDni(), user.getEmail());

        if (exists.isPresent()) throw new RuntimeException("user already exists");

        return this.repository.save(user);
    }

    public Optional<UsersEntity> get(int id) {
        return this.repository.findById(id);
    }
}
