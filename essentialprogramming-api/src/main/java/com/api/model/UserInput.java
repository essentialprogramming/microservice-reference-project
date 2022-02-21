package com.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = " First name ", required = true, example = "Razvan")
    private String firstName;

    @NotNull(message = "LastName field can not be null!")
    @Pattern(regexp = Patterns.NAME_REGEXP, message = "Invalid lastname! Only uppercase and lowercase letters are allowed.")
    @JsonProperty("lastName")
    @Schema(description = " Last name ", required = true, example = "Prichici")
    private String lastName;

    @NotNull(message = "Phone field can not be null!")
    @Size(min = 5, max = 20, message = "Your phone number must have 5 to 20 numbers!")
    @Pattern(regexp = Patterns.PHONE_REGEXP,
            message = "Invalid phone number! Example: +111 (202) 555-0123, +40740536211, 0712345678")
    @JsonProperty("phone")
    @Schema(description = " Phone ", required = true, example = "0745664689")
    private String phone;

    @NotNull(message = "Email field cannot be null!")
    @Email(message = "Email is invalid! Must be of format: example@domain.com",
            regexp = Patterns.EMAIL_REGEXP)
    @JsonProperty("email")
    @Schema(description = " Valid email ", required = true, example = "razvanpaulp@gmail.com")
    private String email;

    @JsonProperty("password")
    @NotNull(message = "Password cannot be null!")
    @Schema(description = " Password ", required = true, example = "test")
    private String password;

    @JsonProperty("confirmPassword")
    @NotNull(message = "Password cannot be null!")
    @Schema(description = " Repeat Password ", required = true, example = "test")
    private String confirmPassword;
}
