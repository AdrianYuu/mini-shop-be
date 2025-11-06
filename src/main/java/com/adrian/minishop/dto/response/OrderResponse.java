package com.adrian.minishop.dto.response;

import com.adrian.minishop.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class OrderResponse {

    private String id;

    private BigDecimal totalPrice;

    private Status status;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
