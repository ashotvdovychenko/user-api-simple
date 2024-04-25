package com.example.user.api.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateDto {
    @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "You can use a-z, 0-9 and dashes for first name")
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "You can use a-z, 0-9 and dashes for last name")
    private String lastName;

    @Email(message = "Enter correct email")
    @Size(min = 5, message = "Enter at least 5 characters")
    private String email;

    @Past(message = "Birth date must be a past")
    private LocalDate birthDate;

    private String address;

    private String phoneNumber;
}