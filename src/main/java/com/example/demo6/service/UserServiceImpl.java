package com.example.demo6.service;

import com.example.demo6.dto.UserDto;
import com.example.demo6.entity.User;
import com.example.demo6.mapper.UserMapper;
import com.example.demo6.mapper.UserMapperImpl;
import com.example.demo6.repository.UserRepoImpl;
import com.example.demo6.repository.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {
    private UserRepository userRepo = new UserRepoImpl();
    private UserMapper mapper = new UserMapperImpl();

    @Override
    public UserDto save(UserDto userDto) throws SQLException {
        userRepo.save(mapper.toEntity(userDto));
        return userDto;
    }

    @Override
    public User getById(Integer id) throws SQLException {
        User user = userRepo.findById(id);
        return (user != null) ? mapper.toDtoUser(user) : null;
    }

    @Override
    public List<User> getAll() throws SQLException {
        List<User> users = userRepo.findAll();
        List<User> userResponses = new ArrayList<>();
        for (User user : users) {
            userResponses.add(mapper.toDtoUser(user));
        }
        return userResponses;
    }

    @Override
    public UserDto update(UserDto userDto) throws SQLException {
        userRepo.update(mapper.toEntity(userDto));
        return userDto;
    }

    @Override
    public void delete(Integer id) throws SQLException {
        userRepo.delete(id);
    }
}