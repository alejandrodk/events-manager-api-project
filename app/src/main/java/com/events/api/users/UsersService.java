package com.events.api.users;

import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private final UsersRepository repository;

    public UsersService(UsersRepository repository) {
        this.repository = repository;
    }

    public UsersEntity create(UsersEntity user) {
        return this.repository.save(user);
    }
}
