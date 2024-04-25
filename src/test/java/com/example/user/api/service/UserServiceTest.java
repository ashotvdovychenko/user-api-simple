package com.example.user.api.service;

import com.example.user.api.storage.UserStorage;
import com.example.user.api.util.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserStorage userStorage;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void init() {
        userService.setMinAge(18);
    }

    @Test
    public void creatingWithAllowedAge() {
        when(userStorage.save(TestData.UNSAVED_USER)).thenReturn(TestData.USER);
        var createdUser = userService.create(TestData.UNSAVED_USER);
        assertThat(createdUser).isNotNull();
    }

    @Test
    public void creatingUserWithNotAllowedAge() {
        var unsavedUser = TestData.getUser(null, LocalDate.now());

        assertThatThrownBy(() -> userService.create(unsavedUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Min age must be equal or higher than 18");
    }

    @Test
    public void updatingWithNewDateIsAllowed() {
        var newDate = LocalDate.of(1995, 1, 1);
        var updatedUser = TestData.getUser(TestData.ID, newDate);
        when(userStorage.update(updatedUser)).thenReturn(updatedUser);

        var user = userService.update(updatedUser);

        assertThat(user).isNotNull();
        assertThat(user.getBirthDate()).isEqualTo(newDate);
    }

    @Test
    public void updatingWithNewDateIsNotAllowed() {
        var updatedUser = TestData.getUser(TestData.ID, LocalDate.now());

        assertThatThrownBy(() -> userService.update(updatedUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Min age must be equal or higher than 18");
    }

    @Test
    public void findingAllUsersByBirthDateRange() {
        var startDate = TestData.USER.getBirthDate().minusYears(1);
        var endDate = TestData.USER.getBirthDate().plusYears(1);

        when(userStorage.findAllByBirthDateRange(startDate, endDate))
                .thenReturn(List.of(TestData.USER));

        var users = userService.findAllByBirthDateRange(startDate, endDate);

        assertThat(users).hasSize(1);
        assertThat(users.get(0)).matches(user ->
                user.getBirthDate().isAfter(startDate) && user.getBirthDate().isBefore(endDate));

    }

    @Test
    public void findingAllUsersWithInvalidRange() {
        var startDate = TestData.USER.getBirthDate().plusYears(1);
        var endDate = TestData.USER.getBirthDate().minusYears(1);

        assertThatThrownBy(() -> userService.findAllByBirthDateRange(startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Date %s is not later than %s".formatted(endDate, startDate));
    }
}
