package com.util.web;


import com.util.text.StringUtils;

import javax.servlet.http.Cookie;
import java.util.Optional;

public class CookieUtils {

    public static Optional<String> extractCookieValueByName(Cookie[] cookies, String name) {
        if (cookies == null || cookies.length < 1) {
            return Optional.empty();
        }
        Cookie sessionCookie = null;
        for (Cookie cookie : cookies) {
            if ((name).equals(cookie.getName())) {
                sessionCookie = cookie;
                break;
            }
        }

        if (sessionCookie == null || StringUtils.isEmpty(sessionCookie.getValue())) {
            return Optional.empty();
        } else {
            return Optional.of(sessionCookie.getValue());
        }

    }

    public static Cookie createHTTPOnlyCookie(String name, String value) {
        final Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(20);
        return cookie;
    }
}
