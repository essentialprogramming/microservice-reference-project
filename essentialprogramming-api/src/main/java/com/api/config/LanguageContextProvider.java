package com.api.config;

import com.util.enums.Language;
import com.util.web.SmartLocaleResolver;
import org.glassfish.hk2.api.Factory;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

public class LanguageContextProvider implements Factory<Language> {

    @Context
    private HttpHeaders httpHeaders;

    @Override
    public Language provide() {
        return SmartLocaleResolver.resolveLanguage(httpHeaders);
    }

    @Override
    public void dispose(Language language) {

    }
}
