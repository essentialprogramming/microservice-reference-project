package com.api.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity(name = "language")
@javax.persistence.Table(name = "Language")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LanguageId", nullable = false, unique = true)
    private int id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Symbol")
    private String symbol;

    @Column(name = "DisplayOrder")
    private int displayOrder;

}
