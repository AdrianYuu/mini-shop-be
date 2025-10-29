package com.adrian.minishop.service;

import com.adrian.minishop.dto.response.ProductCategoryResponse;
import com.adrian.minishop.entity.ProductCategory;
import com.adrian.minishop.mapper.ProductCategoryMapper;
import com.adrian.minishop.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
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

}
