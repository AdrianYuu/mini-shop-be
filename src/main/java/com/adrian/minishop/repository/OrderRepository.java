package com.adrian.minishop.repository;

import com.adrian.minishop.entity.Order;
import com.adrian.minishop.entity.User;
import com.adrian.minishop.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    Page<Order> findAllByUserAndStatus(User user, Status status, Pageable pageable);

    Optional<Order> findFirstByUserAndId(User user, String id);

    Optional<Order> findFirstByUserAndStatus(User user, Status status);

}
