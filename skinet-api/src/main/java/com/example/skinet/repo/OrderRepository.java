package com.example.skinet.repo;

import com.example.skinet.core.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer>, OrderRepositoryCustom {

}
