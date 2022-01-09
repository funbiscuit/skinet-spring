package com.example.skinet.service;

import com.example.skinet.core.entity.CustomerBasket;
import com.example.skinet.repo.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;

    public void deleteBasket(String id) {
        basketRepository.deleteById(id);
    }

    public Optional<CustomerBasket> getBasket(String id) {
        return basketRepository.findById(id);
    }

    public CustomerBasket updateBasket(CustomerBasket basket) {
        return basketRepository.save(basket);
    }
}
