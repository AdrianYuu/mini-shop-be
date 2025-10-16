package com.adrian.minishop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SQLDelete(sql = "UPDATE order_items SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class OrderItem extends BaseEntity {

    @Column(
            name = "quantity",
            nullable = false,
            unique = false,
            updatable = true
    )
    private Integer quantity;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "order_id",
            referencedColumnName = "id",
            nullable = false,
            unique = false,
            updatable = false
    )
    private Order order;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "product_id",
            referencedColumnName = "id",
            nullable = false,
            unique = false,
            updatable = false
    )
    private Product product;

}
