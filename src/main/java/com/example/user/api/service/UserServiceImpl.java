package com.example.user.api.service;

import com.example.user.api.model.User;
import com.example.user.api.storage.UserStorage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    // Temporary in-memory storage using HashMap
    private final UserStorage userStorage;

    @Getter
    @Setter
    @Value("${user.min-age}")
    private int minAge;

    @Override
    public User create(User user) {
        if (isAgeNotAllowed(user.getBirthDate())) {
            throw new IllegalArgumentException(
                    "Min age must be equal or higher than %d".formatted(minAge));
        }
        return userStorage.save(user);
    }

    @Override
    public User update(User updatedUser) {
        if (isAgeNotAllowed(updatedUser.getBirthDate())) {
            throw new IllegalArgumentException(
                    "Min age must be equal or higher than %d".formatted(minAge));
        }
        return userStorage.update(updatedUser);
    }

    @Override
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public Optional<User> findById(String id) {
        return userStorage.findById(id);
    }

    @Override
    public List<User> findAllByBirthDateRange(LocalDate birthDateFrom, LocalDate birthDateTo) {
        if (birthDateFrom.isAfter(birthDateTo)) {
            throw new IllegalArgumentException(
                    "Date %s is not later than %s".formatted(birthDateTo, birthDateFrom));
        }
        return userStorage.findAllByBirthDateRange(birthDateFrom, birthDateTo);
    }

    @Override
    public void deleteById(String id) {
        userStorage.deleteById(id);
    }

    private boolean isAgeNotAllowed(LocalDate birthDate) {
        return birthDate.isAfter(LocalDate.now().minusYears(minAge));
    }
}
