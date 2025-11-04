package com.adrian.minishop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class CreateOrderItemRequest {

    @NotNull(message = "Quantity is required")
    @Min(
            value = 1,
            message = "Quantity must be greater than 0"
    )
    private Integer quantity;

    @NotBlank(
            message = "Product id is required"
    )
    private String productId;

}
