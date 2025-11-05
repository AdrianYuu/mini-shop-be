package com.adrian.minishop.entity;

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
@Table(name = "product_categories")
@Getter
@Setter
@SQLDelete(sql = "UPDATE product_categories SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class ProductCategory extends BaseEntity {

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
            mappedBy = "category"
    )
    private List<Product> products;

}
