package com.adrian.minishop.entity;

import com.adrian.minishop.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
@Setter
@SQLDelete(sql = "UPDATE orders SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Order extends BaseEntity {

    @Column(
            name = "total_price",
            precision = 10,
            scale = 2,
            nullable = false,
            unique = false,
            updatable = true
    )
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            length = 10,
            nullable = false,
            unique = false,
            updatable = true
    )
    private Status status;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            nullable = false,
            unique = false,
            updatable = false
    )
    private User user;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "order"
    )
    private List<OrderItem> items;

}
