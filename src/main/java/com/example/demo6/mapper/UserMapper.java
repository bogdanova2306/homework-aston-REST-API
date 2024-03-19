package com.example.demo6.mapper;


import com.example.demo6.dto.UserDto;
import com.example.demo6.entity.User;

public interface UserMapper {
    User toEntity(UserDto userDto);

    User toDtoUser(User user);
}