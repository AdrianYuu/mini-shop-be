package com.adrian.minishop.controller;

import com.adrian.minishop.dto.request.CreateProductRequest;
import com.adrian.minishop.dto.request.SearchProductRequest;
import com.adrian.minishop.dto.request.UpdateProductRequest;
import com.adrian.minishop.dto.response.PaginationResponse;
import com.adrian.minishop.dto.response.ProductResponse;
import com.adrian.minishop.dto.response.WebResponse;
import com.adrian.minishop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<List<ProductResponse>>> search(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        SearchProductRequest request = SearchProductRequest.builder()
                .name(name)
                .category(category)
                .page(page)
                .size(size)
                .build();

        Page<ProductResponse> response = productService.search(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WebResponse.<List<ProductResponse>>builder()
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
    public ResponseEntity<WebResponse<ProductResponse>> get(@PathVariable("id") String id) {
        ProductResponse response = productService.get(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WebResponse.<ProductResponse>builder()
                        .data(response)
                        .build());
    }

    @PostMapping(
            path = "",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<ProductResponse>> create(@ModelAttribute CreateProductRequest request) {
        ProductResponse response = productService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(WebResponse.<ProductResponse>builder()
                        .data(response)
                        .build());
    }

    @PatchMapping(
            path = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<ProductResponse>> update(
            @PathVariable("id") String id,
            @ModelAttribute UpdateProductRequest request
    ) {
        ProductResponse response = productService.update(id, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WebResponse.<ProductResponse>builder()
                        .data(response)
                        .build());
    }

}
