package com.adrian.minishop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @Column(
            name = "id",
            length = 36,
            nullable = false,
            unique = true,
            updatable = false
    )
    private String id;

    @Column(
            name = "name",
            length = 100,
            nullable = false,
            unique = false,
            updatable = true
    )
    private String name;

    @Column(
            name = "email",
            length = 100,
            nullable = false,
            unique = true,
            updatable = true
    )
    private String email;

    @Column(
            name = "password",
            length = 100,
            nullable = false,
            unique = false,
            updatable = true
    )
    private String password;

    @Column(
            name = "bio",
            length = 255,
            nullable = true,
            unique = false,
            updatable = true
    )
    private String bio;

    @Column(
            name = "image_key",
            length = 100,
            nullable = true,
            unique = true,
            updatable = true
    )
    private String imageKey;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "role",
            length = 10,
            nullable = false,
            unique = false,
            updatable = false
    )
    private Role role;

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            unique = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(
            name = "updated_at",
            nullable = false,
            unique = false,
            updatable = true
    )
    private LocalDateTime updatedAt;

    @Column(
            name = "deleted_at",
            nullable = true,
            unique = false,
            updatable = true
    )
    private LocalDateTime deletedAt;

}
