package com.logging;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class MarkerMap {

    private static MarkerMap INSTANCE = null;
    private final ConcurrentMap<String, Log4jMarker> markerMap;

    private MarkerMap() {
        this.markerMap = new ConcurrentHashMap<>();
    }

    public static MarkerMap getInstance() {
        if (INSTANCE == null) INSTANCE = new MarkerMap();

        return INSTANCE;
    }

    public void put(final String key, final Log4jMarker value) {
        markerMap.put(key, value);
    }

    public void putIfAbsent(final String key, final Log4jMarker value) {
        markerMap.putIfAbsent(key, value);
    }

    public Log4jMarker get(final String key) {
        return markerMap.get(key);
    }

    public boolean containsKey(final String key) {
        return markerMap.containsKey(key);
    }
}
