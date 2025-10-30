package com.adrian.minishop.service;

import com.adrian.minishop.dto.request.SearchProductRequest;
import com.adrian.minishop.dto.response.ProductResponse;
import com.adrian.minishop.entity.Product;
import com.adrian.minishop.mapper.ProductMapper;
import com.adrian.minishop.repository.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ValidationService validationService;

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

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

}
