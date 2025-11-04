package com.adrian.minishop.controller;

import com.adrian.minishop.dto.request.PaginationRequest;
import com.adrian.minishop.dto.response.OrderResponse;
import com.adrian.minishop.dto.response.PaginationResponse;
import com.adrian.minishop.dto.response.WebResponse;
import com.adrian.minishop.entity.User;
import com.adrian.minishop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping(
            path = ""
    )
    public ResponseEntity<WebResponse<List<OrderResponse>>> paginate(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        PaginationRequest request = PaginationRequest.builder()
                .page(page)
                .size(size)
                .build();

        Page<OrderResponse> response = orderService.paginate(user, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WebResponse.<List<OrderResponse>>builder()
                        .data(response.getContent())
                        .pagination(PaginationResponse.builder()
                                .page(response.getNumber() + 1)
                                .size(response.getSize())
                                .totalPages(response.getTotalPages())
                                .totalElements(response.getTotalElements())
                                .build())
                        .build());
    }

}
