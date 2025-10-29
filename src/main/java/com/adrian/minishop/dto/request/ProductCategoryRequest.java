package com.adrian.minishop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class ProductCategoryRequest {

    @NotBlank(message = "Name is required")
    @Size(
            max = 100,
            message = "Name must not exceed 100 characters"
    )
    private String name;

}
