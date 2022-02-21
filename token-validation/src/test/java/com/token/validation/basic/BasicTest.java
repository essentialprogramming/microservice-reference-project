package com.token.validation.basic;

import com.token.validation.auth.AuthUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class BasicTest {


    @Test
    void create_and_parse_basic_header(){
        String basicToken= AuthUtils.createBasicAuthenticationHeader("razvan", "test");
        Map<String, String> usernamePassword = AuthUtils.extractBasicUsernameAndPassword(basicToken);

        Assertions.assertEquals("razvan", usernamePassword.get("username"));
        Assertions.assertEquals("test", usernamePassword.get("password"));
    }
}
