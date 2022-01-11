package com.util.web;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Example Usage:
 * <p>
 * return new JsonResponse()
 * .with("status", "ok")
 * .with("shouldRefresh", true)
 * .done();
 */
public class JsonResponse extends LinkedHashMap<String, Object> {
    public static final long serialVersionUID = 1;

    public JsonResponse() {
    }

    public JsonResponse with(String key, Object value) {
        this.putIfAbsent(key, value);
        return this;
    }

    public JsonResponse with(String key, JsonResponse value) {
        this.putIfAbsent(key, value);
        return this;
    }

    public JsonResponse done() {
        try {
            new ObjectMapper().writeValueAsString(this);
            return this;
        } catch (Exception e) {
            return new JsonResponse().with("Error", "Well, this is embarrassing, we are having trouble generating the response for you !");
        }
    }

    public Map<String, Object> getResponse() {
        return this;
    }

}
