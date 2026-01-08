package com.example.TulipApplication.entities;
import com.example.TulipApplication.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users") // "user" is a reserved word in Postgres, so we use "users"
@Data
public class User extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}