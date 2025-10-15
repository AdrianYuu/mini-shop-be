package com.adrian.minishop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

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
            name = "price",
            precision = 10,
            scale = 2,
            nullable = false,
            unique = false,
            updatable = true
    )
    private BigDecimal price;

    @Column(
            name = "stock",
            nullable = false,
            unique = false,
            updatable = true
    )
    private Integer stock;

    @Column(
            name = "image_key",
            length = 100,
            nullable = true,
            unique = true,
            updatable = true
    )
    private String imageKey;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "category_id",
            referencedColumnName = "id",
            nullable = false,
            unique = false,
            updatable = true
    )
    private ProductCategory category;

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
