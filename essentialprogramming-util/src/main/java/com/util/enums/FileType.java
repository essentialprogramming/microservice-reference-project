package com.util.enums;

public enum FileType {

    CSV(1),
    PDF(2);

    public final Integer value;

    FileType(Integer value) {
        this.value = value;
    }
}
