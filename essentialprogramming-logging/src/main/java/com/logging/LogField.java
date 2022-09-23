package com.logging;


import lombok.NonNull;

import javax.annotation.Nullable;
import java.beans.ConstructorProperties;
import java.util.Objects;

public final class LogField {
    @NonNull
    private final String fieldName;

    @Nullable
    private final Object fieldValue;

    @NonNull
    private final boolean obfuscationRequired;

    @ConstructorProperties({"fieldName", "fieldValue", "obfuscationRequired"})
    public LogField(@lombok.NonNull String fieldName, @Nullable Object fieldValue, boolean obfuscationRequired) {
        if (fieldName.isEmpty()) {
            throw new IllegalArgumentException("FieldName must not be empty");
        }

        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.obfuscationRequired = obfuscationRequired;

    }

    @NonNull
    public String getFieldName() {
        return this.fieldName;
    }

    @Nullable
    public Object getFieldValue() {
        return this.fieldValue;
    }

    @NonNull
    public boolean isObfuscationRequired() {
        return this.obfuscationRequired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogField logField = (LogField) o;
        return obfuscationRequired == logField.obfuscationRequired && fieldName.equals(logField.fieldName) && Objects.equals(fieldValue, logField.fieldValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName, fieldValue, obfuscationRequired);
    }

    @NonNull
    public String toString() {
        return "LogField(fieldName=" + this.getFieldName() + ", fieldValue=" + this.getFieldValue() + ", obfuscationRequired=" + this.isObfuscationRequired() + ")";
    }
}
