package com.adrian.minishop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(
            name = "created_at",
            nullable = false,
            unique = false,
            updatable = false
    )
    private OffsetDateTime createdAt;

    @LastModifiedDate
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

}
