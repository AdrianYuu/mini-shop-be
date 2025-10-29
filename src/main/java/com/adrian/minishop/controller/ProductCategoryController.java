package com.adrian.minishop.controller;

import com.adrian.minishop.dto.request.ProductCategoryRequest;
import com.adrian.minishop.dto.response.ProductCategoryResponse;
import com.adrian.minishop.dto.response.WebResponse;
import com.adrian.minishop.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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

    @PutMapping(
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

}
