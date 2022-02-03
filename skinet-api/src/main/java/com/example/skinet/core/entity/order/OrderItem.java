package com.example.skinet.core.entity.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_item")
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private ProductItemOrdered itemOrdered;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItem(ProductItemOrdered itemOrdered, BigDecimal price, int quantity) {
        this.itemOrdered = itemOrdered;
        this.price = price;
        this.quantity = quantity;
    }
}
