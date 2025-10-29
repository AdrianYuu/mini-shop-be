package com.adrian.minishop.controller;

import com.adrian.minishop.dto.request.CreateProductCategoryRequest;
import com.adrian.minishop.dto.response.ProductCategoryResponse;
import com.adrian.minishop.dto.response.WebResponse;
import com.adrian.minishop.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<WebResponse<List<ProductCategoryResponse>>> list() {
        List<ProductCategoryResponse> response = productCategoryService.list();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WebResponse.<List<ProductCategoryResponse>>builder()
                        .data(response)
                        .build());
    }

    @GetMapping(
            path = "/{productCategoryId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<ProductCategoryResponse>> get(@PathVariable("productCategoryId") String productCategoryId) {
        ProductCategoryResponse response = productCategoryService.get(productCategoryId);

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
    public ResponseEntity<WebResponse<ProductCategoryResponse>> create(@RequestBody CreateProductCategoryRequest request) {
        ProductCategoryResponse response = productCategoryService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(WebResponse.<ProductCategoryResponse>builder()
                        .data(response)
                        .build());
    }

}
