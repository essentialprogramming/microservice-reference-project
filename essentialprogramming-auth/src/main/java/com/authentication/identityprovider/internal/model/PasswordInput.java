package com.authentication.identityprovider.internal.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordInput {

    private String key;
    private String newPassword;
    private String confirmPassword;
}
