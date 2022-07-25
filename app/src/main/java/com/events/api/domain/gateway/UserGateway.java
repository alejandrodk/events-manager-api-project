package com.events.api.domain.gateway;

import com.events.api.data.entity.UsersEntity;
import com.events.api.data.repository.UsersRepository;
import com.events.api.domain.model.User;
import com.events.api.domain.utils.ModelMapperUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserGateway {
    private final UsersRepository usersRepository;

    public UserGateway(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public User save(User user) {
        UsersEntity entity = ModelMapperUtils.mapToClass(user, UsersEntity.class);
        UsersEntity result = this.usersRepository.save(entity);
        return toModel(result);
    }

    public Optional<User> findById(int userId) {
        return this.usersRepository.findById(userId).map(this::toModel);
    }

    public Optional<User> findByDniOrEmail(String dni, String email) {
        return this.usersRepository.findOneByDniOrEmail(dni, email)
                .map(this::toModel);
    }

    private User toModel(UsersEntity entity) {
        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .lastname(entity.getLastname())
                .dni(entity.getDni())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRole())
                .build();
    }
}
