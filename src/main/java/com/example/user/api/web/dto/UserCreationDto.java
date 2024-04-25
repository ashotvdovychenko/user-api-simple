package com.example.user.api.web.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationDto {
    @NotBlank(message = "Specify first name")
    private String firstName;

    @NotBlank(message = "Specify last name")
    private String lastName;

    @NotBlank(message = "Specify email")
    @Email(message = "Enter correct email")
    private String email;

    @NotNull(message = "Specify birth date")
    @Past(message = "Birth date must be a past")
    private LocalDate birthDate;

    private String address;

    private String phoneNumber;
}
