package com.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInput {

    @NotNull(message = "FirstName field can not be null!")
    @Pattern(regexp = Patterns.NAME_REGEXP, message = "Invalid firstname! Only uppercase and lowercase letters are allowed.")
    @JsonProperty("firstName")
    private String firstName;

    @NotNull(message = "LastName field can not be null!")
    @Pattern(regexp = Patterns.NAME_REGEXP, message = "Invalid lastname! Only uppercase and lowercase letters are allowed.")
    @JsonProperty("lastName")
    private String lastName;

    @NotNull(message = "Phone field can not be null!")
    @Size(min = 5, max = 20, message = "Your phone number must have 5 to 20 numbers!")
    @Pattern(regexp = Patterns.PHONE_REGEXP,
            message = "Invalid phone number! Example: +111 (202) 555-0123, +40740536211, 0712345678")
    @JsonProperty("phone")
    private String phone;

    @NotNull(message = "Email field can not be null!")
    @Email(message = "Email is invalid! Must be of format: example@domain.com",
            regexp = Patterns.EMAIL_REGEXP)
    @JsonProperty("email")
    private String email;

    private String password;
    private String confirmPassword;
}
