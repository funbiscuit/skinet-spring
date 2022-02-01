package com.example.skinet.core.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BasketItem implements Serializable {
    @Positive
    private int id;

    @NotBlank
    private String productName;

    @DecimalMin(value = "0.1", message = "must be greater than zero")
    private BigDecimal price;

    @Positive
    private int quantity;

    @NotBlank
    private String pictureUrl;

    @NotBlank
    private String brand;

    @NotBlank
    private String type;
}
