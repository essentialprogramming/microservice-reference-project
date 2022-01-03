package com.util.text;

import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class StringUtilsTest {

    @Test
    void strings_are_not_equal() {
        String germanString = "Entwickeln Sie mit Vergnügen";
        byte[] germanBytes = germanString.getBytes();

        String asciiEncodedString = new String(germanBytes, StandardCharsets.US_ASCII);

        assertNotEquals(asciiEncodedString, germanString);
    }

    @Test
    void strings_are_equal() {
        String rawString = "Entwickeln Sie mit Vergnügen";
        String utf8EncodedString = StringUtils.encodeText(rawString);

        assertEquals(rawString, utf8EncodedString);
    }
}
