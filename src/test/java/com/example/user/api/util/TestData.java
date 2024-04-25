package com.example.user.api.util;

import com.example.user.api.model.User;
import com.example.user.api.web.dto.UserCreationDto;
import com.example.user.api.web.dto.UserUpdateDto;

import java.time.LocalDate;

public final class TestData {
    public static final String ID = "id";
    public static final String FIRST_NAME = "first-name";
    public static final String LAST_NAME = "last-name";
    public static final String EMAIL = "email@gmail.com";
    public static final String ADDRESS = "address";
    public static final String PHONE_NUMBER = "+380975555555";
    public static final LocalDate BIRTH_DATE = LocalDate.of(2000, 1, 1);

    public static final String FIRST_NAME_UPDATE = "FIRST-NAME";
    public static final String LAST_NAME_UPDATE = "LAST-NAME";
    public static final String EMAIL_UPDATE = "email123@gmail.com";
    public static final String ADDRESS_UPDATE = "address123";
    public static final String PHONE_NUMBER_UPDATE = "+380971111111";
    public static final LocalDate BIRTH_DATE_UPDATE = LocalDate.of(1991, 12, 12);

    public static final User USER = getUser(ID, BIRTH_DATE);
    public static final User UNSAVED_USER = getUser(null, BIRTH_DATE);
    public static final UserUpdateDto USER_UPDATE_DTO = getUserUpdateDto(BIRTH_DATE_UPDATE);
    public static final UserCreationDto USER_CREATION_DTO = getUserCreationDto(EMAIL, BIRTH_DATE);
    public static final UserCreationDto USER_FULL_UPDATE_DTO = getUserFullUpdateDto(BIRTH_DATE_UPDATE);

    public static User getUser(String id, LocalDate birthDate) {
        return new User(id, FIRST_NAME, LAST_NAME, EMAIL, birthDate, ADDRESS, PHONE_NUMBER);
    }

    public static UserUpdateDto getUserUpdateDto(LocalDate birthDate) {
        return new UserUpdateDto(FIRST_NAME, LAST_NAME, EMAIL, birthDate, ADDRESS, PHONE_NUMBER);
    }

    public static UserCreationDto getUserCreationDto(String email, LocalDate birthDate) {
        return new UserCreationDto(FIRST_NAME, LAST_NAME, email, birthDate, ADDRESS, PHONE_NUMBER);
    }

    public static UserCreationDto getUserFullUpdateDto(LocalDate birthDate) {
        return new UserCreationDto(FIRST_NAME_UPDATE, LAST_NAME_UPDATE, EMAIL_UPDATE,
                birthDate, ADDRESS_UPDATE, PHONE_NUMBER_UPDATE);
    }
}
