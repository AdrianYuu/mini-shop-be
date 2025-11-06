package com.adrian.minishop.dto.mapper;

import com.adrian.minishop.dto.response.OrderItemResponse;
import com.adrian.minishop.dto.response.OrderItemSimpleResponse;
import com.adrian.minishop.entity.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemResponse orderItemToOrderItemResponse(OrderItem orderItem);

    OrderItemSimpleResponse orderItemToOrderItemSimpleResponse(OrderItem orderItem);

}
