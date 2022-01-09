package com.example.skinet.core.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RedisHash(timeToLive = 60 * 60 * 24)
@Data
@NoArgsConstructor
public class CustomerBasket implements Serializable {

    private String id;

    private List<BasketItem> items = new ArrayList<>();

    public CustomerBasket(String id) {
        this.id = id;
    }
}
