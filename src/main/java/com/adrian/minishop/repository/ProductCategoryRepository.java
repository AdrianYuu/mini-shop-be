package com.adrian.minishop.repository;

import com.adrian.minishop.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String> {

    boolean existsByName(String name);

}
