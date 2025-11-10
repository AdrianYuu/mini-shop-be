package com.adrian.minishop.dto.request;

import com.adrian.minishop.core.validation.annotation.AtLeastOneFieldNotNull;
import com.adrian.minishop.core.validation.annotation.ValidFile;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@AtLeastOneFieldNotNull
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class UpdateProductRequest {

    @Size(
            max = 100,
            message = "Name must not exceed 100 characters"
    )
    private String name;

    @DecimalMin(
            value = "0.0",
            message = "Price must be greater than 0",
            inclusive = false
    )
    private BigDecimal price;

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

    private String categoryId;

    // (form-data [category_id] -> [categoryId])
    public void setCategory_Id(String categoryId) {
        this.categoryId = categoryId;
    }

}
