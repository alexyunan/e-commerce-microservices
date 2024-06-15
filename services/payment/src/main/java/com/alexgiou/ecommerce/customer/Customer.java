package com.alexgiou.ecommerce.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record Customer(
        String id,
        @NotBlank(message = "First name is mandatory")
        String firstName,
        @NotBlank(message = "Last name is mandatory")
        String lastName,
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email is invalid")
        String email
) {
}
