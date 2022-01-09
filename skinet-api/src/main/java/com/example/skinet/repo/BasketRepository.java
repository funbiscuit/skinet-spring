package com.example.skinet.repo;

import com.example.skinet.core.entity.CustomerBasket;
import org.springframework.data.repository.CrudRepository;

public interface BasketRepository extends CrudRepository<CustomerBasket, String> {
}
