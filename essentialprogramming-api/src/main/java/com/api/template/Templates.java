package com.api.template;

import com.template.model.Template;

import java.util.Optional;

public enum Templates implements Template {

    PARENT_HTML("html/parent"),

    NEW_USER("html/new_user", "new_user", PARENT_HTML),
    PDF_EXAMPLE("html/example");

    public String page;
    public String fragment = null;
    public Templates master = null;

    Templates(String page) {
        this.page = page;
    }

    Templates(String page, String fragment, Templates master) {
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
