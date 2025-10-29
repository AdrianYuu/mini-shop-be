package com.adrian.minishop.service;

import com.adrian.minishop.dto.response.ProductCategoryResponse;
import com.adrian.minishop.entity.ProductCategory;
import com.adrian.minishop.exception.HttpException;
import com.adrian.minishop.mapper.ProductCategoryMapper;
import com.adrian.minishop.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ValidationService validationService;

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductCategoryMapper productCategoryMapper;

    public List<ProductCategoryResponse> list() {
        List<ProductCategory> productCategories = productCategoryRepository.findAll();

        return productCategories.stream()
                .map(productCategoryMapper::productCategoryToProductCategoryResponse)
                .toList();
    }

    public ProductCategoryResponse get(String productCategoryId) {
        ProductCategory productCategory = productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Product category not found"));

        return productCategoryMapper.productCategoryToProductCategoryResponse(productCategory);
    }

}
