package com.example.skinet.repo;

import com.example.skinet.core.entity.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryCustom {
    List<Order> getUserOrders(String email);

    Optional<Order> getUserOrderById(String email, Integer id);
}
