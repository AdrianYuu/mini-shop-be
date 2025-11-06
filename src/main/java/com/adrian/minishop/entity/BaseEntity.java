package com.adrian.minishop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@SuperBuilder
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @UuidGenerator(style = Style.RANDOM)
    @Column(
            name = "id",
            length = 36,
            nullable = false,
            unique = true,
            updatable = false,
            columnDefinition = "CHAR(36)"
    )
    private String id;

    @Column(
            name = "created_at",
            nullable = false,
            unique = false,
            updatable = false
    )
    private OffsetDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false,
            unique = false,
            updatable = true
    )
    private OffsetDateTime updatedAt;

    @Column(
            name = "deleted_at",
            nullable = true,
            unique = false,
            updatable = true
    )
    private OffsetDateTime deletedAt;

    @PrePersist
    protected void preCreate() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
        updatedAt = now;
        createdAt = now;
    }

}
