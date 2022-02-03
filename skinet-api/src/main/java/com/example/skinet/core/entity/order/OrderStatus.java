package com.example.skinet.core.entity.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
    Pending("Pending"),
    PaymentReceived("Payment Received"),
    PaymentFailed("Payment Failed");

    @Getter
    private final String value;
}
