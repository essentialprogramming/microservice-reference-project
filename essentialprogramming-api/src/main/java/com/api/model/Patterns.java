package com.api.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Patterns {
    public static final String NAME_REGEXP =  "[a-zA-Z]+";
    public static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEXP);

    public static final String PHONE_REGEXP =  "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";
    public static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEXP);

    public static final String EMAIL_REGEXP = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEXP);
}
