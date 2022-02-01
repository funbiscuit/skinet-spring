package com.example.skinet.core.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class RegisterDTO {
    @NotNull
    @Pattern(regexp = ".+@.+", message = "Not a valid email address")
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])\\S{8,24}$",
            message = "Password must contain uppercase and lowercase letters, a digit and be at least 8 chars long")
    private String password;

    @NotBlank
    private String name;
}
