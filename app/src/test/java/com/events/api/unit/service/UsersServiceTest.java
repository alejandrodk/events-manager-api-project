package com.events.api.unit.service;

import com.events.api.domain.gateway.UserGateway;
import com.events.api.domain.model.User;
import com.events.api.domain.service.UsersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {
    @Mock
    UserGateway userGateway;

    @InjectMocks
    UsersService usersService;

    @Test
    public void shouldReturnUser() {
        int userId = 1;
        User user = User.builder().id(userId).name("foo").build();

        when(usersService.get(userId)).thenReturn(Optional.of(user));

        Optional<User> result = usersService.get(userId);

        assertThat(result.isPresent(), equalTo(true));
        assertThat(result.get(), equalToObject(user));
    }

    @Test
    public void shouldCreateUserIfNotExist() {
        User user = User.builder().id(1).dni("123").email("123").build();

        when(userGateway.findByDniOrEmail(user.getDni(), user.getEmail()))
                .thenReturn(Optional.empty());

        when(userGateway.save(user)).thenReturn(user);

        User result = usersService.create(user);

        assertThat(result, notNullValue());
        assertThat(result, equalToObject(user));
    }

    @Test
    public void shouldThrowExceptionIfUserAlreadyExists() {
        try {
            User user = User.builder().id(1).dni("123").email("123").build();

            when(userGateway.findByDniOrEmail(user.getDni(), user.getEmail()))
                    .thenReturn(Optional.of(user));

            usersService.create(user);
        } catch (Exception e) {
            assertThat(e, notNullValue());
            assertThat(e.getMessage(), equalTo("user already exists"));
        }
    }
}
