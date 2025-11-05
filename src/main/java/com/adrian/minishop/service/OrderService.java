package com.adrian.minishop.service;

import com.adrian.minishop.dto.request.CreateOrderItemRequest;
import com.adrian.minishop.dto.request.PaginationRequest;
import com.adrian.minishop.dto.request.UpdateOrderItemRequest;
import com.adrian.minishop.dto.response.OrderItemResponse;
import com.adrian.minishop.dto.response.OrderItemSimpleResponse;
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
import com.adrian.minishop.util.TimeUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    private final TimeUtil timeUtil;

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
    public OrderItemSimpleResponse create(User user, CreateOrderItemRequest request) {
        validationService.validate(request);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Product not found"));

        if (request.getQuantity() > product.getStock()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Insufficient stock for product");
        }

        Order order = getOrCreateActiveOrder(user);

        boolean orderItemExists = orderItemRepository.existsByOrderAndProduct(order, product);

        if (orderItemExists) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Product already exists in active order");
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(request.getQuantity());
        orderItem.setOrder(order);
        orderItem.setProduct(product);

        orderItem = orderItemRepository.save(orderItem);

        return orderItemMapper.orderItemToOrderItemSimpleResponse(orderItem);
    }

    @Transactional
    public OrderItemSimpleResponse update(User user, String id, UpdateOrderItemRequest request) {
        validationService.validate(request);

        Order order = getOrCreateActiveOrder(user);

        OrderItem orderItem = orderItemRepository.findFirstByOrderAndId(order, id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Order item not found in active order"));

        Product product = orderItem.getProduct();

        if (request.getQuantity() > product.getStock()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Insufficient stock for product");
        }

        orderItem.setQuantity(request.getQuantity());
        orderItem.setUpdatedAt(timeUtil.now());

        orderItem = orderItemRepository.save(orderItem);

        return orderItemMapper.orderItemToOrderItemSimpleResponse(orderItem);
    }

    @Transactional
    public void delete(User user, String id) {
        Order order = getOrCreateActiveOrder(user);

        OrderItem orderItem = orderItemRepository.findFirstByOrderAndId(order, id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Order item not found in active order"));

        orderItemRepository.delete(orderItem);
    }

    @Transactional
    public OrderResponse checkout(User user) {
        Order order = getOrCreateActiveOrder(user);

        if (order.getItems().isEmpty()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Order items is empty");
        }

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItem orderItem : order.getItems()) {
            Product product = productRepository.findByIdForUpdate(orderItem.getProduct().getId())
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Product not found"));

            if (orderItem.getQuantity() > product.getStock()) {
                throw new HttpException(HttpStatus.BAD_REQUEST, "Insufficient stock for product with the name: " + product.getName());
            }

            product.setStock(product.getStock() - orderItem.getQuantity());
            productRepository.save(product);

            BigDecimal subTotalPrice = product.getPrice()
                    .multiply(BigDecimal.valueOf(orderItem.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);
            totalPrice = totalPrice.add(subTotalPrice);
        }

        order.setTotalPrice(totalPrice.setScale(2, RoundingMode.HALF_UP));
        order.setStatus(Status.FINALIZED);
        order.setUpdatedAt(timeUtil.now());

        order = orderRepository.save(order);

        return orderMapper.orderToOrderResponse(order);
    }

    @Transactional
    public Order getOrCreateActiveOrder(User user) {
        Order order = orderRepository.findFirstByUserAndStatus(user, Status.ACTIVE)
                .orElse(null);

        if (Objects.nonNull(order)) {
            return order;
        }

        order = new Order();
        order.setTotalPrice(BigDecimal.ZERO);
        order.setStatus(Status.ACTIVE);
        order.setUser(user);

        order = orderRepository.save(order);

        return order;
    }

}
