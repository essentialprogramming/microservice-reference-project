package com.authentication.template;

import com.template.model.Template;

import java.util.Optional;

public enum TemplateEnum implements Template {

    PARENT_HTML("html/parent"),
    OTP_LOGIN("html/otp_login", "otp_login", PARENT_HTML);


    public String page;
    public String fragment = null;
    public TemplateEnum master = null;

    TemplateEnum(String page) {
        this.page = page;
    }

    TemplateEnum(String page, String fragment, TemplateEnum master) {
        this.page = page;
        this.fragment = fragment;
        this.master = master;
    }

    public Optional<String> getPage() {
        return Optional.of(page);
    }

    public Optional<String> getFragment() {
        return Optional.ofNullable(fragment);
    }

    public Optional<Template> getMaster() {
        return Optional.ofNullable(master);
    }
}
