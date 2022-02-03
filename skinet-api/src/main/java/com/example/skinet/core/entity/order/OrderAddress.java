package com.example.skinet.core.entity.order;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class OrderAddress {

    private String firstName;

    private String lastName;

    private String street;

    private String city;

    private String state;

    private String zipCode;

    private String country;
}
