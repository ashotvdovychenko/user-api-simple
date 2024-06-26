package com.example.user.api.web.dto;

import lombok.Data;

@Data
public class UserDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String birthDate;
    private String address;
    private String phoneNumber;
}