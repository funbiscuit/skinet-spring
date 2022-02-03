package com.example.skinet.core.entity.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "buyer_email")
    private String buyerEmail;

    @Column(name = "order_date")
    private ZonedDateTime orderDate = ZonedDateTime.now();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "ship_to_first_name")),
            @AttributeOverride(name = "lastName", column = @Column(name = "ship_to_last_name")),
            @AttributeOverride(name = "street", column = @Column(name = "ship_to_street")),
            @AttributeOverride(name = "city", column = @Column(name = "ship_to_city")),
            @AttributeOverride(name = "state", column = @Column(name = "ship_to_state")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "ship_to_zip_code")),
            @AttributeOverride(name = "country", column = @Column(name = "ship_to_country")),
    })
    private OrderAddress shipToAddress;

    @ManyToOne
    @JoinColumn(name = "delivery_method_id")
    private DeliveryMethod deliveryMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @Column(name = "subtotal")
    private BigDecimal subtotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status = OrderStatus.Pending;

    @Column(name = "payment_intent_id")
    private String paymentIntentId;

    public Order(List<OrderItem> orderItems, String buyerEmail, OrderAddress shipToAddress,
                 DeliveryMethod deliveryMethod, BigDecimal subtotal) {
        this.buyerEmail = buyerEmail;
        this.shipToAddress = shipToAddress;
        this.deliveryMethod = deliveryMethod;
        this.orderItems = orderItems;
        this.subtotal = subtotal;

        orderItems.forEach(item -> item.setOrder(this));
    }

    public BigDecimal getTotal() {
        return subtotal.add(deliveryMethod.getPrice());
    }
}
