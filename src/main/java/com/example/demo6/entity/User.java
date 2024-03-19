package com.example.demo6.entity;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
    private Integer id;
    private String userName;
    private String userSurname;
    private String role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId().equals(user.getId()) && getUserName().equals(user.getUserName()) &&
                Objects.equals(getUserSurname(), user.getUserSurname()) && Objects.equals(getRole(), user.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserName(), getUserSurname(), getRole());
    }

    @Override
    public String toString(){
        return String.valueOf(id);
    }
}