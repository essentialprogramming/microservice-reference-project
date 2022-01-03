package com.template.model;

import java.util.Optional;

public interface Template {

    Optional<String> getPage();

    Optional<String> getFragment();

    Optional<Template> getMaster();
}
