package com.adrian.minishop.repository;

import com.adrian.minishop.entity.Order;
import com.adrian.minishop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    Page<Order> findAllByUser(User user, Pageable pageable);

    Optional<Order> findByUserAndId(User user, String id);

}
