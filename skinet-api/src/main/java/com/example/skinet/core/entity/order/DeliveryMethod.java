package com.example.skinet.core.entity.order;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "delivery_method")
public class DeliveryMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "delivery_time")
    private String deliveryTime;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;
}
