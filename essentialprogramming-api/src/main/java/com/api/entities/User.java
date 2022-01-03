package com.api.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
@javax.persistence.Table(name = "User")
public class User {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DefaultLanguageId")
    private Language language;

    @Column(name = "Validated")
    private boolean validated;

    @Column(name = "UserKey")
    private String userKey;

    @Column(name = "ModifiedDate")
    private LocalDateTime modifiedDate;

    @Column(name = "Active")
    private boolean active;

    @Column(name = "Deleted")
    private boolean deleted;

    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;

    @Column(name = "ModifiedBy")
    private Integer modifiedBy;

    @Column(name = "CreatedBy")
    private Integer createdBy;

    @Column(name = "Password")
    private String password;



    public String getFullName() {
        return Optional.ofNullable(firstName).orElse("") + " " + Optional.ofNullable(lastName).orElse("");
    }

}
