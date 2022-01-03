package com.api.mapper;

import com.api.entities.User;
import com.api.model.UserInput;
import com.api.output.UserJSON;


public class UserMapper {

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
                .build();
    }


}
