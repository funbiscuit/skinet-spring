package com.example.skinet.repo;

import com.example.skinet.core.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("select distinct o from Order o join fetch o.orderItems items " +
            "join fetch o.deliveryMethod dm where o.buyerEmail = ?1 " +
            "order by o.orderDate desc")
    List<Order> getUserOrders(String email);

    @Query("select o from Order o join fetch o.orderItems items " +
            "join fetch o.deliveryMethod dm where o.buyerEmail = ?1 and o.id = ?2")
    Optional<Order> getUserOrderById(String email, Integer id);
}
