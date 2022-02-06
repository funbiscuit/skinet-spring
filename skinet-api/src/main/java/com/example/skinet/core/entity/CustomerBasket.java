package com.example.skinet.core.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RedisHash(timeToLive = 60 * 60 * 24)
@Data
@NoArgsConstructor
public class CustomerBasket implements Serializable {

    @NotBlank
    private String id;

    @Valid
    private List<BasketItem> items = new ArrayList<>();

    private Integer deliveryMethodId;

    private BigDecimal shippingPrice;

    private String clientSecret;

    private String paymentIntentId;

    public CustomerBasket(String id) {
        this.id = id;
    }
}
