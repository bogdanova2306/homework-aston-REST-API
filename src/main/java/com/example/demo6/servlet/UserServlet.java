package com.example.demo6.servlet;

import com.example.demo6.dto.UserDto;
import com.example.demo6.service.UserService;
import com.example.demo6.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private UserService userService = new UserServiceImpl();
    private ObjectMapper objectMapper;
    public UserServlet() {
        this.objectMapper = new ObjectMapper();
    }

    public UserServlet(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = initResp(resp, HttpServletResponse.SC_OK).getWriter();
        String id = req.getParameter("id");
        if (id != null) {
            if (!isValid(id)) {
                initResp(resp, HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Пустой параметр");
                return;
            }
            try {
                Integer.parseInt(id);
            } catch (NumberFormatException e) {
                initResp(resp, HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Невалидный формат id");
                return;
            }
            try {
                out.print(new Gson().toJson(userService.getById(Integer.valueOf((req.getParameter("id"))))));
            } catch (SQLException e) {
                initResp(resp, HttpServletResponse.SC_BAD_REQUEST).getWriter();
                out.print(e.getMessage());
            }
        } else {
            try {
                out.print(userService.getAll().stream().map(userDto -> new Gson().toJson(userDto)).toList());
            } catch (SQLException e) {
                initResp(resp, HttpServletResponse.SC_NO_CONTENT).getWriter();
                out.print(e.getMessage());
            }
        }
        out.flush();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        StringBuilder jsonBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBody.append(line);
        }
        UserDto userDto = objectMapper.readValue(jsonBody.toString(), UserDto.class);
        UserDto createdUser;
        try {
            createdUser = userService.save(userDto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(resp.getWriter(), createdUser);
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        StringBuilder jsonBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBody.append(line);
        }
        UserDto userDto = objectMapper.readValue(jsonBody.toString(), UserDto.class);
        UserDto updatedUser;
        try {
            updatedUser = userService.update(userDto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(resp.getWriter(), updatedUser);
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = initResp(resp, HttpServletResponse.SC_OK).getWriter();

        String id = req.getParameter("id");
        if (!isValid(id)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Пустой параметр id");
            return;
        }
        try {
            Integer.parseInt(id);
        } catch (NumberFormatException e) {
            initResp(resp, HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Невалидный формат id");
            return;
        }
        try {
            userService.delete(Integer.valueOf(id));
            out.println("Успешно удалено");
        } catch (SQLException e) {
            out = initResp(resp, HttpServletResponse.SC_BAD_REQUEST).getWriter();
            out.print(e.getMessage());
        }
        out.flush();
    }

    private static HttpServletResponse initResp(HttpServletResponse resp, int status) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(status);
        return resp;
    }

    private static boolean isValid(String parameter) {
        return parameter != null && !parameter.isEmpty();
    }
}