package com.adrian.minishop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    @Id
    @Column(
            name = "id",
            length = 36,
            nullable = false,
            unique = true,
            updatable = false
    )
    private String id;

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
