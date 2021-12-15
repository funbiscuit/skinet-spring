package com.example.skinet.core.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private int id;

    private String name;

    private String description;

    private String pictureUrl;

    private BigDecimal price;

    private String type;

    private String brand;
}
