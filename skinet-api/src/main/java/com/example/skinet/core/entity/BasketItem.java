package com.example.skinet.core.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BasketItem implements Serializable {
    private int id;

    private String productName;

    private BigDecimal price;

    private int quantity;

    private String pictureUrl;

    private String brand;

    private String type;
}
