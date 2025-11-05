package com.adrian.minishop.service;

import com.adrian.minishop.dto.request.CreateOrderItemRequest;
import com.adrian.minishop.dto.request.PaginationRequest;
import com.adrian.minishop.dto.response.OrderItemResponse;
import com.adrian.minishop.dto.response.OrderResponse;
import com.adrian.minishop.entity.Order;
import com.adrian.minishop.entity.OrderItem;
import com.adrian.minishop.entity.Product;
import com.adrian.minishop.entity.User;
import com.adrian.minishop.enums.Status;
import com.adrian.minishop.exception.HttpException;
import com.adrian.minishop.mapper.OrderItemMapper;
import com.adrian.minishop.mapper.OrderMapper;
import com.adrian.minishop.repository.OrderItemRepository;
import com.adrian.minishop.repository.OrderRepository;
import com.adrian.minishop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ValidationService validationService;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final ProductRepository productRepository;

    private final OrderMapper orderMapper;

    private final OrderItemMapper orderItemMapper;

    public Page<OrderResponse> paginate(User user, PaginationRequest request) {
        validationService.validate(request);

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Page<Order> page = orderRepository.findAllByUserAndStatus(user, Status.FINALIZED, pageable);

        return page.map(orderMapper::orderToOrderResponse);
    }

    public OrderResponse get(User user, String id) {
        Order order = orderRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Order not found"));

        return orderMapper.orderToOrderResponse(order);
    }

    public OrderResponse active(User user) {
        Order order = getOrCreateActiveOrder(user);

        return orderMapper.orderToOrderResponse(order);
    }

    public List<OrderItemResponse> items(User user, String id) {
        Order order = orderRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Order not found"));

        List<OrderItem> orderItems = orderItemRepository.findAllByOrder(order);

        return orderItems.stream()
                .map(orderItemMapper::orderItemToOrderItemResponse)
                .toList();
    }

    @Transactional
    public OrderItemResponse create(User user, CreateOrderItemRequest request) {
        validationService.validate(request);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Product not found"));

        if (request.getQuantity() > product.getStock()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Insufficient stock for product");
        }

        Order order = getOrCreateActiveOrder(user);

        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(request.getQuantity());
        orderItem.setOrder(order);
        orderItem.setProduct(product);

        orderItem = orderItemRepository.save(orderItem);

        return orderItemMapper.orderItemToOrderItemResponse(orderItem);
    }

    @Transactional
    public Order getOrCreateActiveOrder(User user) {
        Order order = orderRepository.findFirstByUserAndStatus(user, Status.ACTIVE)
                .orElse(null);

        if (Objects.nonNull(order)) {
            return order;
        }

        order = new Order();
        order.setTotalPrice(new BigDecimal("0.00"));
        order.setStatus(Status.ACTIVE);
        order.setUser(user);

        order = orderRepository.save(order);

        return order;
    }

}
