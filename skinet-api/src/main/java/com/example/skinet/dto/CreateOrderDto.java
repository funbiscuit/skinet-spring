package com.example.skinet.dto;

import com.example.skinet.core.entity.AddressDTO;
import lombok.Data;

@Data
public class CreateOrderDto {
    private String basketId;
    private Integer deliveryMethodId;
    private AddressDTO shippingAddress;
}
