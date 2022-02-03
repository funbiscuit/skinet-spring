package com.example.skinet.dto;

import com.example.skinet.core.entity.order.OrderAddress;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Integer id;

    private String buyerEmail;

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ZonedDateTime orderDate;

    private OrderAddress shipToAddress;

    private String deliveryMethod;

    private BigDecimal shippingPrice;

    private List<OrderItemDto> orderItems;

    private BigDecimal subtotal;

    private BigDecimal total;

    private String status;
}
