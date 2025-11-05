package com.adrian.minishop.controller;

import com.adrian.minishop.dto.request.CreateOrderItemRequest;
import com.adrian.minishop.dto.request.PaginationRequest;
import com.adrian.minishop.dto.request.UpdateOrderItemRequest;
import com.adrian.minishop.dto.response.*;
import com.adrian.minishop.entity.User;
import com.adrian.minishop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
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

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<OrderResponse>> get(
            @AuthenticationPrincipal User user,
            @PathVariable("id") String id
    ) {
        OrderResponse response = orderService.get(user, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WebResponse.<OrderResponse>builder()
                        .data(response)
                        .build());
    }

    @GetMapping(
            path = "/active",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<OrderResponse>> active(
            @AuthenticationPrincipal User user
    ) {
        OrderResponse response = orderService.active(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WebResponse.<OrderResponse>builder()
                        .data(response)
                        .build());
    }

    @GetMapping(
            path = "/{id}/items",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<List<OrderItemResponse>>> items(
            @AuthenticationPrincipal User user,
            @PathVariable("id") String id
    ) {
        List<OrderItemResponse> response = orderService.items(user, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WebResponse.<List<OrderItemResponse>>builder()
                        .data(response)
                        .build());
    }

    @PostMapping(
            path = "/active/items",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<OrderItemSimpleResponse>> create(
            @AuthenticationPrincipal User user,
            @RequestBody CreateOrderItemRequest request
    ) {
        OrderItemSimpleResponse response = orderService.create(user, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(WebResponse.<OrderItemSimpleResponse>builder()
                        .data(response)
                        .build());
    }

    @PatchMapping(
            path = "/active/items/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<OrderItemSimpleResponse>> update(
            @AuthenticationPrincipal User user,
            @PathVariable("id") String id,
            @RequestBody UpdateOrderItemRequest request
    ) {
        OrderItemSimpleResponse response = orderService.update(user, id, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WebResponse.<OrderItemSimpleResponse>builder()
                        .data(response)
                        .build());
    }

    @DeleteMapping(
            path = "/active/items/{id}"
    )
    public ResponseEntity<WebResponse<Void>> delete(
            @AuthenticationPrincipal User user,
            @PathVariable("id") String id
    ) {
        orderService.delete(user, id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
