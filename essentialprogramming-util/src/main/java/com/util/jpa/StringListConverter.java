package com.util.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * This attribute converter converts a List of String into a comma-separated String and vice versa<br/>
 * <br/>
 * <b>Example:<b><br/>
 *
 * <pre>
 *  "a,b,c" <=> ["a","b","c"]
 *  "a" <=> ["a"]
 *  null = []
 *  ""   = []
 * </pre>
 *
 */

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(List<String> stringList) {
        if (stringList != null){
            return String.join(SPLIT_CHAR, stringList);
        }
        else{
            return null;
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String input) {
        if (input != null){
            return Collections.list(new StringTokenizer(input.trim(), SPLIT_CHAR)).stream().map(token -> (String) token)
                    .collect(Collectors.toList());
        } else{
            return new ArrayList<>();
        }
    }
}