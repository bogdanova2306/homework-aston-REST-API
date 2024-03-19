package com.example.demo6.repository;

import com.example.demo6.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {
    void save(User user) throws SQLException;
    User findById(Integer id) throws SQLException;
    List<User> findAll() throws SQLException;
    void update(User user) throws SQLException;
    void delete(Integer id) throws SQLException;
}