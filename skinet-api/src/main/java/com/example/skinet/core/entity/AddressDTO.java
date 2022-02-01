package com.example.skinet.core.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddressDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String street;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String country;
}
