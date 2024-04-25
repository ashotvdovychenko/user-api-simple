package com.example.user.api.web.mapper;

import com.example.user.api.model.User;
import com.example.user.api.web.dto.UserCreationDto;
import com.example.user.api.web.dto.UserDto;
import com.example.user.api.web.dto.UserUpdateDto;
import org.mapstruct.*;

@Mapper
public interface UserMapper {
    @Mapping(target = "birthDate", source = "birthDate", dateFormat = "yyyy-MM-dd")
    UserDto toPayload(User user);

    User toEntity(UserCreationDto userDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserUpdateDto userDto, @MappingTarget User user);

    User fullUpdate(UserCreationDto userDto, @MappingTarget User user);
}
