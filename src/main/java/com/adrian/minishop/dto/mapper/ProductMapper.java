package com.adrian.minishop.dto.mapper;

import com.adrian.minishop.dto.response.ProductResponse;
import com.adrian.minishop.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse productToProductResponse(Product product);

}
