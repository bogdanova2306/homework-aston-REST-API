package com.example.demo6.repository;

import com.example.demo6.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo6.utils.Util.*;

public class UserRepoImpl implements UserRepository {

    @Override
    public void save(User user) throws SQLException {
        String sql = "INSERT INTO users (id, username, usersurname, role) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, String.valueOf(user.getId()));
            statement.setString(2, user.getUserName());
            statement.setString(3, user.getUserSurname());
            statement.setString(4, user.getRole());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Не удалось создать пользователя");
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public User findById(Integer id) throws SQLException {
        User user = null;
        String sql = "SELECT id, username, usersurname, role FROM users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (user == null) {
                  user = mapUser(resultSet);
                }
            }
            if (user == null) {
                throw new SQLException("Не удалось найти пользователя с id " + id);
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return user;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM users";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            Map<Integer, User> userMap = new HashMap<>();
            while (resultSet.next()) {
                Integer userId = resultSet.getInt("id");
                User user = userMap.get(userId);
                if (user == null) {
                    user = mapUser(resultSet);
                    users.add(user);
                    userMap.put(userId, user);
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return users;
    }

    @Override
    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, usersurname = ?, role = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getUserSurname());
            statement.setString(3, user.getRole());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Пользователя с id " + user.getId() + " не существует");
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Пользователя с id " + id + " не существует");
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    private User mapUser(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .userName(resultSet.getString("username"))
                .userSurname(resultSet.getString("usersurname"))
                .role(resultSet.getString("role"))
                .build();
    }
}