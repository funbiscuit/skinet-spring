package com.example.skinet.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {

    private Integer productId;

    private String productName;

    private String pictureUrl;

    private BigDecimal price;

    private int quantity;


}
