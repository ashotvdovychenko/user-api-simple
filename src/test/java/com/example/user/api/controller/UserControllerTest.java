package com.example.user.api.controller;

import com.example.user.api.storage.UserStorage;
import com.example.user.api.util.TestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserStorage userStorage;

    private final ObjectMapper mapper = new ObjectMapper();

    private final String url = "users";

    @AfterEach
    public void cleanAll() {
        userStorage.deleteAll();
    }

    @Test
    public void findByIdWhenExists() throws Exception {
        var id = userStorage.save(TestData.getUser(null, LocalDate.now())).getId();

        var result = mockMvc.perform(get("/{url}/{id}", url, id));

        result.andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(id));
    }

    @Test
    public void findByIdWhenNotExists() throws Exception {
        var result = mockMvc.perform(get("/{url}/{id}", url, TestData.ID));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByBirthDateRangeWithSomeUsersInRange() throws Exception {
        var from = "1998-01-01";
        var to = "2002-01-01";
        userStorage.save(TestData.getUser(null, LocalDate.of(2000, 1, 1)));

        var result = mockMvc
                .perform(get("/{url}?birth_date_from={from}&birth_date_to={to}", url, from, to));

        result.andExpectAll(
                status().isOk(),
                jsonPath("$").value(hasSize(1)));
    }

    @Test
    public void findByBirthDateRangeWithNoUsersInRange() throws Exception {
        var from = "2007-01-01";
        var to = "2012-01-01";

        var result = mockMvc
                .perform(get("/{url}?birth_date_from={from}&birth_date_to={to}", url, from, to));

        result.andExpectAll(
                status().isOk(),
                jsonPath("$").value(hasSize(0)));
    }

    @Test
    public void findByBirthDateRangeWithInvalidRange() throws Exception {
        var from = "2002-01-01";
        var to = "1998-01-01";

        var result = mockMvc
                .perform(get("/{url}?birth_date_from={from}&birth_date_to={to}", url, from, to));

        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").value("Date %s is not later than %s".formatted(to, from)));
    }

    @Test
    public void creatingWithValidValues() throws Exception {
        mapper.findAndRegisterModules();
        var json = mapper.writeValueAsString(TestData.USER_CREATION_DTO);

        var result = mockMvc.perform(post("/{url}", url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpectAll(
                status().isCreated(),
                jsonPath("$.firstName").value(TestData.FIRST_NAME),
                jsonPath("$.lastName").value(TestData.LAST_NAME),
                jsonPath("$.email").value(TestData.EMAIL),
                jsonPath("$.birthDate").value(TestData.BIRTH_DATE.toString()),
                jsonPath("$.phoneNumber").value(TestData.PHONE_NUMBER),
                jsonPath("$.address").value(TestData.ADDRESS));
    }

    @Test
    public void creatingWithInvalidEmail() throws Exception {
        mapper.findAndRegisterModules();
        var json = mapper.writeValueAsString(TestData.getUserCreationDto("Invalid Email", LocalDate.now().minusYears(1)));

        var result = mockMvc.perform(post("/{url}", url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").value("Enter correct email"));
    }

    @Test
    public void creatingWithNotAllowedBirthDate() throws Exception {
        mapper.findAndRegisterModules();
        var json = mapper.writeValueAsString(TestData.getUserCreationDto(TestData.EMAIL, LocalDate.now().minusYears(1)));

        var result = mockMvc.perform(post("/{url}", url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").value("Min age must be equal or higher than 18"));
    }

    @Test
    public void partialUpdatingByIdWithAllowedBirthDate() throws Exception {
        mapper.findAndRegisterModules();
        var json = mapper.writeValueAsString(TestData.USER_UPDATE_DTO);
        var id = userStorage.save(TestData.getUser(null, LocalDate.now())).getId();

        var result = mockMvc.perform(patch("/{url}/{id}", url, id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(id),
                jsonPath("$.birthDate").value(TestData.BIRTH_DATE_UPDATE.toString()));
    }

    @Test
    public void partialUpdatingByIdWithNotAllowedBirthDate() throws Exception {
        mapper.findAndRegisterModules();
        var json = mapper.writeValueAsString(TestData.getUserUpdateDto(LocalDate.now().minusYears(1)));
        var id = userStorage.save(TestData.getUser(null, LocalDate.now())).getId();

        var result = mockMvc.perform(patch("/{url}/{id}", url, id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").value("Min age must be equal or higher than 18"));
    }

    @Test
    public void fullUpdatingByIdWithAllowedBirthDate() throws Exception {
        mapper.findAndRegisterModules();
        var json = mapper.writeValueAsString(TestData.USER_FULL_UPDATE_DTO);
        var id = userStorage.save(TestData.UNSAVED_USER).getId();

        var result = mockMvc.perform(put("/{url}/{id}", url, id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(id),
                jsonPath("$.firstName").value(TestData.FIRST_NAME_UPDATE),
                jsonPath("$.lastName").value(TestData.LAST_NAME_UPDATE),
                jsonPath("$.email").value(TestData.EMAIL_UPDATE),
                jsonPath("$.birthDate").value(TestData.BIRTH_DATE_UPDATE.toString()),
                jsonPath("$.phoneNumber").value(TestData.PHONE_NUMBER_UPDATE),
                jsonPath("$.address").value(TestData.ADDRESS_UPDATE));
    }


    @Test
    public void fullUpdatingSelfWithNotAllowedBirthDate() throws Exception {
        mapper.findAndRegisterModules();
        var json = mapper.writeValueAsString(TestData.getUserFullUpdateDto(LocalDate.now().minusYears(1)));
        var id = userStorage.save(TestData.getUser(null, LocalDate.now())).getId();

        var result = mockMvc.perform(put("/{url}/{id}", url, id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").value("Min age must be equal or higher than 18"));
    }

    @Test
    public void deletingByIdWhenExists() throws Exception {
        var id = userStorage.save(TestData.getUser(null, LocalDate.now())).getId();

        var result = mockMvc.perform(delete("/{url}/{id}", url, id));
        var deletedUser = userStorage.findById(id);

        result.andExpect(status().isNoContent());
        assertThat(deletedUser).isEmpty();
    }

    @Test
    public void deletingByIdWhenNotExists() throws Exception {
        var result = mockMvc.perform(delete("/{url}/{id}", url, TestData.ID));
        var deletedUser = userStorage.findById(TestData.ID);

        result.andExpect(status().isNoContent());
        assertThat(deletedUser).isEmpty();
    }
}


