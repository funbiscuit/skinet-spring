package com.example.skinet.controller;

import com.example.skinet.core.entity.CustomerBasket;
import com.example.skinet.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("basket")
public class BasketController {
    private final BasketService basketService;

    @GetMapping
    public ResponseEntity<CustomerBasket> getBasket(String id) {
        return ResponseEntity.ok(basketService.getBasket(id)
                .orElse(new CustomerBasket(id)));
    }

    @PostMapping
    public ResponseEntity<CustomerBasket> updateBasket(@RequestBody @Valid CustomerBasket basket) {
        return ResponseEntity.ok(basketService.updateBasket(basket));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteBasket(String id) {
        basketService.deleteBasket(id);
        return ResponseEntity.ok().build();
    }
}
