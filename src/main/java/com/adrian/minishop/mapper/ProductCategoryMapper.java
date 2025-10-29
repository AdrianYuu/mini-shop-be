package com.adrian.minishop.mapper;

import com.adrian.minishop.dto.response.ProductCategoryResponse;
import com.adrian.minishop.entity.ProductCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {

    ProductCategoryResponse productCategoryToProductCategoryResponse(ProductCategory productCategory);

}
