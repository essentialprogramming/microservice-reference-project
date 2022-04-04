package com.util.jpa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class StringListConverterTest {

    @Test
    public void string_list_successful_conversion() {
        final StringListConverter stringListConverter = new StringListConverter();
        final String convertedList = stringListConverter.convertToDatabaseColumn(Arrays.asList("1", "2"));

        assertThat(stringListConverter.convertToEntityAttribute(convertedList)).isEqualTo(Arrays.asList("1", "2"));
    }
}
