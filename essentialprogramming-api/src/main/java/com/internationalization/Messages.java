package com.internationalization;

import com.spring.ApplicationContextFactory;
import com.util.enums.Language;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;


public class Messages {

	private static final MessageSource messageSource = ApplicationContextFactory.getBean("messageSource", ReloadableResourceBundleMessageSource.class);

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
		return messageSource.getMessage(key, null, locale);
	}

	public static String get(String key, Object[] params, Locale locale) {
		return messageSource.getMessage(key, params, locale);
	}

}
