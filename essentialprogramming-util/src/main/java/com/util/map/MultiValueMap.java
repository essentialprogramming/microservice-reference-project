package com.util.map;

import java.util.List;
import java.util.Map;

/**
 * This class represents a map of String keys to a List of String values.
 *
 * It's useful to represent things like HTTP headers and HTTP parameters which allow multiple values
 * for keys.
 *
 * @author Razvan Prichici
 */
public interface MultiValueMap<K, V> extends Map<K, List<V>> {

    /**
     * Return the first value for the given key.
     *
     * @param key the key
     * @return the first value for the specified key, or {@code null}
     */
    V getFirst(K key);

    /**
     * Add a value to the current list of values for the supplied key.
     *
     * @param key the key
     * @param value the value to be added
     */
    void add(K key, V value);

    /**
     * Add the given values to the current list of values for the given key.
     *
     * @param key the key
     * @param value the value to be added
     */
    void add(K key, V[] value);


    /**
     * Add the given values to the current list of values for the given key.
     *
     * @param key the key
     * @param value the value to be added
     */
    void add(K key, List<V> value);

    /**
     * Set the given single value under the given key.
     *
     * @param key the key
     * @param value the value to set
     */
    void set(K key, V value);

}