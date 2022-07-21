package com.events.api.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@AllArgsConstructor
public class UsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private String lastname;
    @Column
    private String dni;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String role;
}
