package com.adrian.minishop.dto.mapper;

import com.adrian.minishop.dto.response.OrderResponse;
import com.adrian.minishop.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse orderToOrderResponse(Order order);

}
