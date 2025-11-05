package com.adrian.minishop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class OrderItemSimpleResponse {

    private String id;

    private Integer quantity;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
