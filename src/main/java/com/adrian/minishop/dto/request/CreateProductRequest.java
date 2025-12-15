package com.adrian.minishop.dto.request;

import com.adrian.minishop.core.validation.annotation.ValidFile;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class CreateProductRequest {

    @NotBlank(message = "Name is required")
    @Size(
            max = 100,
            message = "Name must not exceed 100 characters"
    )
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(
            value = "0.0",
            message = "Price must be greater than 0",
            inclusive = false
    )
    private BigDecimal price;

    @NotNull(message = "Stock is required")
    @Min(
            value = 1,
            message = "Stock must be greater than 0"
    )
    private Integer stock;

    @ValidFile(
            maxSize = 2_000_000,
            contentTypes = {"image/jpg", "image/jpeg", "image/png"},
            message = "Image must be JPG, JPEG or PNG under 2MB",
            required = false
    )
    private MultipartFile image;

    @NotBlank(
            message = "Category id is required"
    )
    private String categoryId;

    // (form-data [category_id] -> [categoryId])
    public void setCategory_id(String categoryId) {
        this.categoryId = categoryId;
    }

}
