package com.adrian.minishop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "products")
@Getter
@Setter
@SQLDelete(sql = "UPDATE products SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Product extends BaseEntity {

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
            unique = false,
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

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "product"
    )
    private List<OrderItem> items;

}
