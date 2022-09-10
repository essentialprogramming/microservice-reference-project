package com.logging;


import java.util.HashMap;

public class LoggingContext extends HashMap<String, String> {

    public LoggingContext with(final String key, final String value){
        this.put(key, value);
        return this;
    }

}