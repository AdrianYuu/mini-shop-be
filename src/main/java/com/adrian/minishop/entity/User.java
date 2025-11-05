package com.adrian.minishop.entity;

import com.adrian.minishop.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "users")
@Getter
@Setter
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class User extends BaseEntity {

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
            unique = false,
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
            unique = false,
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

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "user"
    )
    private List<Order> orders;

}
