package com.authentication.identityprovider.internal.entities;


import lombok.*;
import javax.persistence.*;
import java.util.List;
import java.util.Optional;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "account")
@Table(name = "User")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId", nullable = false, unique = true)
    private int id;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "Email")
    private String email;

    @Column(name = "Phone")
    private String phone;


    @Column(name = "Validated")
    private boolean validated;

    @Column(name = "Active")
    private boolean active;

    @Column(name = "Deleted")
    private boolean deleted;

    @Column(name = "UserKey")
    private String userKey;

    @Column(name = "Password")
    private String password;


    public Account(String email,  String userKey,String firstName, String lastName,  String phone) {
        this.email = email;
        this.userKey = userKey;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public String getFullName() {
        return Optional.ofNullable(firstName).orElse("") + " " + Optional.ofNullable(lastName).orElse("");
    }
}
