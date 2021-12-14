package com.example.skinet.core.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "product_types")
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
}
