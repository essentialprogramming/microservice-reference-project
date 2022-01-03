package com.internationalization;

import com.spring.ApplicationContextFactory;
import com.util.enums.Language;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

public class EmailMessages {

    private static final MessageSource messageSourceEmail = ApplicationContextFactory.getBean("messageSourceEmail", ReloadableResourceBundleMessageSource.class);

    public static String get(String key, Language language) {
        final Language defaultLanguage = language != null ? language : Language.ENGLISH;
        return get(key, defaultLanguage.getLocale());
    }

    public static String get(String key, com.api.entities.Language language) {
        return get(key, Language.fromId(language.getId()));
    }

    public static String get(String key, int languageId) {
        return get(key, Language.fromId(languageId));
    }

    public static String get(String key, Object[] params, Language language) {
        return get(key, params, language.getLocale());
    }

    public static String get(String key, Locale locale) {
        return messageSourceEmail.getMessage(key, null, locale);
    }

    public static String get(String key, Object[] params, Locale locale) {
        return messageSourceEmail.getMessage(key, params, locale);
    }
}
