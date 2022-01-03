package com.util.collection;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionUtils {

    @SafeVarargs
    public static <T> List<T> concat(Collection<T>... lists) {
        return Stream.of(lists).filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * Concatenates multiple character arrays together.
     *
     * @param  first  array to concatenate. Cannot be null.
     * @param  rest  of the arrays to concatenate. May be null.
     *
     * @return  array containing the concatenation of all parameters
     */
    public static char[] concatArrays(final char[] first, final char[]... rest)
    {
        int totalLength = first.length;
        for (char[] array : rest) {
            if (array != null) {
                totalLength += array.length;
            }
        }

        final char[] result = Arrays.copyOf(first, totalLength);

        int offset = first.length;
        for (char[] array : rest) {
            if (array != null) {
                System.arraycopy(array, 0, result, offset, array.length);
                offset += array.length;
            }
        }
        return result;
    }
}
