package com.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;



@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInput {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private String confirmPassword;


}
