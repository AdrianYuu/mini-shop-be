package com.adrian.minishop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product_categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductCategory {

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

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "products"
    )
    private List<Product> products;

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
