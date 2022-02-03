package com.example.skinet.repo;

import com.example.skinet.core.entity.order.DeliveryMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryMethodRepository extends JpaRepository<DeliveryMethod, Integer> {
}
