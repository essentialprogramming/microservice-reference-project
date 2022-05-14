package com.util.collection;

import java.util.*;
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

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        final List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort((o1, o2) -> o1.getValue().compareTo(o2.getValue()));

        final Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

}
