package com.adrian.minishop.service;

import com.adrian.minishop.dto.request.PaginationRequest;
import com.adrian.minishop.dto.request.ProductCategoryRequest;
import com.adrian.minishop.dto.response.ProductCategoryResponse;
import com.adrian.minishop.entity.ProductCategory;
import com.adrian.minishop.exception.HttpException;
import com.adrian.minishop.mapper.ProductCategoryMapper;
import com.adrian.minishop.repository.ProductCategoryRepository;
import com.adrian.minishop.util.TimeUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ValidationService validationService;

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductCategoryMapper productCategoryMapper;

    private final TimeUtil timeUtil;

    public Page<ProductCategoryResponse> paginate(PaginationRequest request) {
        validationService.validate(request);

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Page<ProductCategory> page = productCategoryRepository.findAll(pageable);

        return page.map(productCategoryMapper::productCategoryToProductCategoryResponse);
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
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Product category not found"));

        validationService.validate(request);

        boolean nameExists = productCategoryRepository.existsByNameAndIdNot(request.getName(), id);

        if (nameExists) {
            throw new HttpException(HttpStatus.CONFLICT, "Name already exists", "name");
        }

        productCategory.setName(request.getName());
        productCategory.setUpdatedAt(timeUtil.now());

        productCategory = productCategoryRepository.save(productCategory);

        return productCategoryMapper.productCategoryToProductCategoryResponse(productCategory);
    }

    @Transactional
    public void delete(String id) {
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Product category not found"));

        productCategoryRepository.delete(productCategory);
    }

}
