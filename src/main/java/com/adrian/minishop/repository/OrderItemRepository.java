package com.adrian.minishop.repository;

import com.adrian.minishop.entity.Order;
import com.adrian.minishop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

    List<OrderItem> findAllByOrder(Order order);

}
