package com.example.user.api.web.controller;

import com.example.user.api.service.UserService;
import com.example.user.api.web.dto.UserCreationDto;
import com.example.user.api.web.dto.UserDto;
import com.example.user.api.web.dto.UserUpdateDto;
import com.example.user.api.web.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.findAll().stream()
                .map(userMapper::toPayload)
                .toList());
    }

    @GetMapping(params = {"birth_date_from", "birth_date_to"})
    public ResponseEntity<List<UserDto>> findByBirthDateRange(@RequestParam(name = "birth_date_from")
                                                              LocalDate birthDateFrom,
                                                              @RequestParam(name = "birth_date_to")
                                                              LocalDate birthDateTo) {
        return ResponseEntity.ok(userService.findAllByBirthDateRange(birthDateFrom, birthDateTo).stream()
                .map(userMapper::toPayload)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable String id) {
        return ResponseEntity.of(userService.findById(id).map(userMapper::toPayload));
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserCreationDto userCreationDto) {
        var newUser = userService.create(userMapper.toEntity(userCreationDto));
        return new ResponseEntity<>(userMapper.toPayload(newUser), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> partialUpdateById(@RequestBody @Valid UserUpdateDto userDto,
                                                     @PathVariable String id) {
        return ResponseEntity.of(userService.findById(id)
                .map(user -> userMapper.partialUpdate(userDto, user))
                .map(userService::update)
                .map(userMapper::toPayload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> fullUpdateById(@RequestBody @Valid UserCreationDto userDto,
                                                  @PathVariable String id) {
        return ResponseEntity.of(userService.findById(id)
                .map(user -> userMapper.fullUpdate(userDto, user))
                .map(userService::update)
                .map(userMapper::toPayload));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
