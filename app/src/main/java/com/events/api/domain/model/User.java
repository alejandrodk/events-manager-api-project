package com.events.api.domain.model;

import lombok.*;

@Builder
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String lastname;
    private String dni;
    private String email;
    private String password;
    private String role = "client";
}
