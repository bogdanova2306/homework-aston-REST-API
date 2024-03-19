package com.example.demo6.service;

import com.example.demo6.dto.UserDto;
import com.example.demo6.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    UserDto save(UserDto userDto) throws SQLException;
    User getById(Integer id) throws SQLException;
    List<User> getAll() throws SQLException;
    UserDto update(UserDto userDto) throws SQLException;
    void delete(Integer id) throws SQLException;
}