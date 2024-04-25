package com.example.user.api.storage;

import com.example.user.api.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class UserStorage {
    private final Map<String, User> userStorage = new HashMap<>();

    public List<User> findAll() {
        return new ArrayList<>(userStorage.values());
    }

    public List<User> findAllByBirthDateRange(LocalDate birthDateFrom, LocalDate birthDateTo) {
        return userStorage.values()
                .stream()
                .filter(user -> user.getBirthDate().isAfter(birthDateFrom) && user.getBirthDate().isBefore(birthDateTo))
                .toList();
    }

    public Optional<User> findById(String id) {
        return Optional.ofNullable(userStorage.get(id));
    }

    public User save(User user) {
        var id = UUID.randomUUID().toString();
        user.setId(id);
        userStorage.put(id, user);
        return userStorage.get(user.getId());
    }

    public User update(User updatedUser) {
        userStorage.replace(updatedUser.getId(), updatedUser);
        return userStorage.get(updatedUser.getId());
    }

    public void deleteById(String id) {
        userStorage.remove(id);
    }

    public void deleteAll() {
        userStorage.clear();
    }
}
