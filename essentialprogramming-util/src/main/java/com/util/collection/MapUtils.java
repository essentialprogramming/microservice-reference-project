package com.util.collection;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MapUtils {

    public static Map<String, Object> flatMap(final Map<String, Object> map) {
        final Map<String, Object> collector = new HashMap<>();
        map.entrySet()
                .stream()
                .flatMap(MapUtils::flatten).forEach(element -> collector.putIfAbsent((String) element.getKey(), element.getValue()));

        return collector;
    }

    private static Stream<Map.Entry<?, ?>> flatten(final Map.Entry<?, ?> entry) {
        if (entry.getValue() instanceof Map<?, ?>) {
            return Stream.concat(Stream.of(new AbstractMap.SimpleEntry<>(entry.getKey(), "")),
                    ((Map<?, ?>) entry.getValue()).entrySet().stream().flatMap(MapUtils::flatten));
        }

        return Stream.of(entry);
    }
}
