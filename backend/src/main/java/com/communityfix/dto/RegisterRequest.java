package com.communityfix.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Block is required")
    private String block;

    @NotBlank(message = "Flat number is required")
    @Pattern(regexp = "^[A-E]-\\d{3}$", message = "Format: Block-Number e.g. A-101")
    private String flatNumber;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Please enter a valid 10-digit phone number")
    private String phoneNumber;

    @NotNull(message = "Move-in date is required")
    private LocalDate moveInDate;
}
