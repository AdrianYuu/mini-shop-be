package com.adrian.minishop.service;

import com.adrian.minishop.dto.request.ProductCategoryRequest;
import com.adrian.minishop.dto.response.ProductCategoryResponse;
import com.adrian.minishop.entity.ProductCategory;
import com.adrian.minishop.exception.HttpException;
import com.adrian.minishop.mapper.ProductCategoryMapper;
import com.adrian.minishop.repository.ProductCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
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

    public ProductCategoryResponse get(String id) {
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Product category not found"));

        return productCategoryMapper.productCategoryToProductCategoryResponse(productCategory);
    }

    @Transactional
    public ProductCategoryResponse create(ProductCategoryRequest request) {
        validationService.validate(request);

        boolean nameExists = productCategoryRepository.existsByName(request.getName());

        if (nameExists) {
            throw new HttpException(HttpStatus.CONFLICT, "Name already exists", "name");
        }

        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(request.getName());

        productCategory = productCategoryRepository.save(productCategory);

        return productCategoryMapper.productCategoryToProductCategoryResponse(productCategory);
    }

    @Transactional
    public ProductCategoryResponse update(String id, ProductCategoryRequest request) {
        validationService.validate(request);

        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Product category not found"));

        boolean nameExists = productCategoryRepository.existsByNameAndIdNot(request.getName(), id);

        if (nameExists) {
            throw new HttpException(HttpStatus.CONFLICT, "Name already exists", "name");
        }

        productCategory.setName(request.getName());
        productCategory.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS));

        productCategory = productCategoryRepository.save(productCategory);

        return productCategoryMapper.productCategoryToProductCategoryResponse(productCategory);
    }

}
