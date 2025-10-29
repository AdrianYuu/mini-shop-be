package com.adrian.minishop.repository;

import com.adrian.minishop.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String> {

    boolean existsByName(String name);

    Optional<ProductCategory> findFirstByName(String name);

    boolean existsByNameAndIdNot(String name, String id);

}
