package com.adrian.minishop.service;

import com.adrian.minishop.dto.request.CreateProductRequest;
import com.adrian.minishop.dto.request.SearchProductRequest;
import com.adrian.minishop.dto.request.UpdateProductRequest;
import com.adrian.minishop.dto.response.ProductResponse;
import com.adrian.minishop.entity.Product;
import com.adrian.minishop.entity.ProductCategory;
import com.adrian.minishop.exception.HttpException;
import com.adrian.minishop.mapper.ProductMapper;
import com.adrian.minishop.repository.ProductCategoryRepository;
import com.adrian.minishop.repository.ProductRepository;
import com.adrian.minishop.util.TimeUtil;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ValidationService validationService;

    private final MinioService minioService;

    private final ProductRepository productRepository;

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductMapper productMapper;

    private final TimeUtil timeUtil;

    public Page<ProductResponse> search(SearchProductRequest request) {
        validationService.validate(request);

        Specification<Product> specification = ((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getName())) {
                predicates.add(builder.like(root.get("name"), "%" + request.getName() + "%"));
            }

            if (Objects.nonNull(request.getCategory())) {
                predicates.add(builder.like(root.get("category").get("name"), "%" + request.getCategory() + "%"));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        });

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Page<Product> page = productRepository.findAll(specification, pageable);

        return page.map(productMapper::productToProductResponse);
    }

    public ProductResponse get(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Product not found"));

        return productMapper.productToProductResponse(product);
    }

    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        ProductCategory productCategory = productCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Product category not found"));

        validationService.validate(request);

        String key = null;
        if (Objects.nonNull(request.getImage())) {
            key = minioService.uploadFile(request.getImage(), minioService.getProductBucket());
        }

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .stock(request.getStock())
                .imageKey(key)
                .category(productCategory)
                .build();

        product = productRepository.save(product);

        return productMapper.productToProductResponse(product);
    }

    @Transactional
    public ProductResponse update(String id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Product not found"));

        validationService.validate(request);

        if (Objects.nonNull(request.getName())) {
            product.setName(request.getName());
        }

        if (Objects.nonNull(request.getPrice())) {
            product.setPrice(request.getPrice());
        }

        if (Objects.nonNull(request.getStock())) {
            product.setStock(request.getStock());
        }

        if (Objects.nonNull(request.getImage())) {
            if (Objects.nonNull(product.getImageKey())) {
                minioService.removeFile(product.getImageKey());
            }

            String key = minioService.uploadFile(request.getImage(), minioService.getProductBucket());
            product.setImageKey(key);
        }

        if (Objects.nonNull(request.getCategoryId())) {
            ProductCategory productCategory = productCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Product category not found"));

            product.setCategory(productCategory);
        }

        product.setUpdatedAt(timeUtil.now());

        product = productRepository.save(product);

        return productMapper.productToProductResponse(product);
    }

    @Transactional
    public void delete(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Product not found"));

        productRepository.delete(product);
    }

}
