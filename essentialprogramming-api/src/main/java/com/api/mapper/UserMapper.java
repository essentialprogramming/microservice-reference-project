package com.api.mapper;

import com.api.entities.User;
import com.api.model.UserInput;
import com.api.output.UserJSON;
import java.time.format.DateTimeFormatter;


public class UserMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static User inputToUser(UserInput input) {
        return User.builder()
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .email(input.getEmail())
                .phone(input.getPhone())
                .build();
    }

    public static UserJSON userToJson(User user) {
        return UserJSON.builder()
                .email(user.getEmail())
                .userKey(user.getUserKey())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .createdDate(user.getCreatedDate() != null ? user.getCreatedDate().format(formatter) : null)
                .build();
    }
}
