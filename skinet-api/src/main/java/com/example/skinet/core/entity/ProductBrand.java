package com.example.skinet.core.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "product_brands")
public class ProductBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
}
