package com.example.demo6.dto;

import com.example.demo6.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

    private Integer id;
    private String userName;
    private String userSurname;
    private String role;

    public UserDto(User user){
        this.id = user.getId();
        this.userName = user.getUserName();
        this.userSurname = user.getUserSurname();
        this.role = user.getRole();
    }

    public UserDto(Integer id, String userName, String userSurname, String role) {
        this.id = id;
        this.userName = userName;
        this.userSurname = userSurname;
        this.role = role;
    }
}