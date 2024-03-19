package com.example.demo6.mapper;

import com.example.demo6.dto.UserDto;
import com.example.demo6.entity.User;

public class UserMapperImpl implements UserMapper {
    @Override
    public User toEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .userName(userDto.getUserName())
                .userSurname(userDto.getUserSurname())
                .role(userDto.getRole())
                .build();
    }

    public User toDtoUser(User user) {
        return User.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .userSurname(user.getUserSurname())
                .role(user.getRole())
                .build();
    }
}