package com.example.user.api.service;

import com.example.user.api.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(User user);

    User update(User updatedUser);

    List<User> findAll();

    Optional<User> findById(String id);

    List<User> findAllByBirthDateRange(LocalDate birthDateFrom, LocalDate birthDateTo);

    void deleteById(String id);
}
