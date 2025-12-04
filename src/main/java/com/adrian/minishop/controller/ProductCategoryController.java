package com.adrian.minishop.controller;

import com.adrian.minishop.dto.request.PaginationRequest;
import com.adrian.minishop.dto.request.ProductCategoryRequest;
import com.adrian.minishop.dto.response.PaginationResponse;
import com.adrian.minishop.dto.response.ProductCategoryResponse;
import com.adrian.minishop.dto.response.WebResponse;
import com.adrian.minishop.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-categories")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @GetMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<List<ProductCategoryResponse>>> paginate(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        PaginationRequest request = PaginationRequest.builder()
                .page(page)
                .size(size)
                .build();

        Page<ProductCategoryResponse> response = productCategoryService.paginate(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WebResponse.<List<ProductCategoryResponse>>builder()
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
            path = "/list",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<List<ProductCategoryResponse>>> list() {
        List<ProductCategoryResponse> response = productCategoryService.list();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WebResponse.<List<ProductCategoryResponse>>builder()
                        .data(response)
                        .build());
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<ProductCategoryResponse>> get(@PathVariable("id") String id) {
        ProductCategoryResponse response = productCategoryService.get(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WebResponse.<ProductCategoryResponse>builder()
                        .data(response)
                        .build());
    }

    @PostMapping(
            path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<ProductCategoryResponse>> create(@RequestBody ProductCategoryRequest request) {
        ProductCategoryResponse response = productCategoryService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(WebResponse.<ProductCategoryResponse>builder()
                        .data(response)
                        .build());
    }

    @PatchMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<ProductCategoryResponse>> update(
            @PathVariable("id") String id,
            @RequestBody ProductCategoryRequest request
    ) {
        ProductCategoryResponse response = productCategoryService.update(id, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WebResponse.<ProductCategoryResponse>builder()
                        .data(response)
                        .build());
    }

    @DeleteMapping(
            path = "/{id}"
    )
    public ResponseEntity<WebResponse<Void>> delete(@PathVariable("id") String id) {
        productCategoryService.delete(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
