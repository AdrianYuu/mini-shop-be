package com.adrian.minishop.repository;

import com.adrian.minishop.entity.Order;
import com.adrian.minishop.entity.OrderItem;
import com.adrian.minishop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

    List<OrderItem> findAllByOrder(Order order);

    Optional<OrderItem> findFirstByOrderAndId(Order order, String id);

    boolean existsByOrderAndProduct(Order order, Product product);

}
