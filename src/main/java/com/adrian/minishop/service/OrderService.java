package com.adrian.minishop.service;

import com.adrian.minishop.dto.request.PaginationRequest;
import com.adrian.minishop.dto.response.OrderResponse;
import com.adrian.minishop.entity.Order;
import com.adrian.minishop.entity.User;
import com.adrian.minishop.mapper.OrderMapper;
import com.adrian.minishop.repository.OrderItemRepository;
import com.adrian.minishop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ValidationService validationService;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final OrderMapper orderMapper;

    public Page<OrderResponse> paginate(User user, PaginationRequest request) {
        validationService.validate(request);

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Page<Order> page = orderRepository.findAllByUser(user, pageable);

        return page.map(orderMapper::orderToOrderResponse);
    }

}
