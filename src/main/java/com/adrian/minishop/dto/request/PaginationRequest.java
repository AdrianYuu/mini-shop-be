package com.adrian.minishop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class PaginationRequest {

    @NotNull(message = "Page is required")
    @Min(
            value = 1,
            message = "Page must be greater than 0"
    )
    private Integer page;

    @NotNull(message = "Size is required")
    @Min(
            value = 1,
            message = "Size must be greater than 0"
    )
    private Integer size;

}
