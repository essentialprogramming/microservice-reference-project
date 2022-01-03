package com.util.enums;

import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.stream.Stream;

@Getter
public enum Language {

    ENGLISH(1, Locale.ENGLISH),
    GERMAN(2, Locale.GERMAN),
    ROMANIAN(3, new Locale("ro"));

    private final Integer id;
    private final Locale locale;

    Language(Integer id, Locale locale) {
        this.id = id;
        this.locale = locale;
    }

    public static Language fromId(int id) {
        return Stream.of(Language.values())
                .filter(language -> language.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No language with id : " + id + " found."));
    }


    public static Language fromLocaleString(String locale) {

        if (StringUtils.isEmpty(locale)) {
            throw new IllegalArgumentException("Locale must not be empty or null.");
        }

        switch (locale) {
            case "en":
            case "en-US":
                return ENGLISH;
            case "de":
            case "de-DE":
                return GERMAN;
            case "ro":
            case "ro-RO":
                return ROMANIAN;
            default:
                throw new IllegalArgumentException("No Language support for locale : " + locale);
        }
    }
}
