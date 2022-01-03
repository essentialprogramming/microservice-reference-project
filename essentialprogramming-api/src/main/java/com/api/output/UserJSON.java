package com.api.output;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserJSON implements Serializable {

    private String email;
    private String userKey;
    private String firstName;
    private String lastName;
    private String phone;

}
