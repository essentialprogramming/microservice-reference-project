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
@Table(name = "users")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    private int id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;


    @Column(name = "validated")
    private boolean validated;

    @Column(name = "active")
    private boolean active;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "user_key")
    private String userKey;

    @Column(name = "password")
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
