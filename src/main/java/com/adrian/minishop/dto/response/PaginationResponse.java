package com.adrian.minishop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class PaginationResponse {

    private Integer page;

    private Integer size;

    private Integer totalPages;

    private Integer totalElements;

}
