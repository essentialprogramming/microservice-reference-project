package com.util.cloud;

public interface Configuration {

    boolean isEmpty();

    boolean containsKey(String key);

    Object getProperty(String key);

    void addProperty(String key, String value);

    String getPropertyAsString(String key);

    Integer getPropertyAsInteger(String key);

    Long getPropertyAsLong(String key);

    Boolean getPropertyAsBoolean(String key);

    void cleanProperties();
}
