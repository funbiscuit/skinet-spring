package com.example.skinet.core.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    @Column(name = "picture_url")
    private String pictureUrl;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private ProductType type;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private ProductBrand brand;

}
