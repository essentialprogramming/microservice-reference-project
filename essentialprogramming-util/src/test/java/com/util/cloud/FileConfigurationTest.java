package com.util.cloud;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;


class FileConfigurationTest {

    private static final String STRING_PROPERTY_KEY = "string.property.key";
    private static final String STRING_PROPERTY_VALUE = "someValue";
    private static final String CLASSPATH = "classpath";

    private Configuration configuration;

    @BeforeEach
    void setUp() {
        System.setProperty("CONFIG_FILE", CLASSPATH + ":" + "application.properties");
        configuration = ConfigurationManager.getConfiguration();
    }

    @Test
    void configuration_properties_are_not_empty() {
        Assertions.assertFalse(configuration.isEmpty());
    }

    @Test
    void configuration_contains_property_key() {
        Assertions.assertTrue(configuration.containsKey(STRING_PROPERTY_KEY));
        Assertions.assertFalse(configuration.containsKey("different.key"));
    }

    @Test
    void successfully_retrieve_property_value() {
        Assertions.assertNotNull(configuration.getProperty(STRING_PROPERTY_KEY));

        String value = configuration.getPropertyAsString(STRING_PROPERTY_KEY);
        Assertions.assertEquals(STRING_PROPERTY_VALUE, value);
    }

    @Test
    void successfully_add_a_new_property() {
        Assertions.assertNull(configuration.getProperty("test.property"));

        configuration.addProperty("test.property", "test.value");
        Assertions.assertNotNull(configuration.getProperty("test.property"));
        Assertions.assertEquals(configuration.getProperty("test.property"), "test.value");
    }


    @Test
    void successfully_get_an_integer_property() {
        Object value = configuration.getPropertyAsInteger("integer.property.key");

        Assertions.assertNotNull(value);
        Assertions.assertEquals(7, value);
    }

    @Test
    void throw_exception_for_invalid_integer_value() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> configuration.getPropertyAsInteger(STRING_PROPERTY_KEY));

        Assertions.assertTrue(exception.getMessage().contains("Property with key '" + STRING_PROPERTY_KEY + "' and value '"
                + STRING_PROPERTY_VALUE + "' can not be converted to int."));
    }
}
