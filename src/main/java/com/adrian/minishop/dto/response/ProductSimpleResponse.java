package com.adrian.minishop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class ProductSimpleResponse {

    private String id;

    private String name;

    private BigDecimal price;

    private Integer stock;

    private String imageKey;

    private ProductCategorySimpleResponse category;

}
