package com.util.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.util.io.FileInputResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Common Util class to be able to use the same Jackson mapper everywhere.
 */
@SuppressWarnings("unused")
public class JsonUtil {

    /**
     * Convert a json string into a java object
     */
    public static <T> T fromJson(final String json, final Class<T> clazz) throws JsonProcessingException {
        return createObjectMapper().readValue(json, clazz);

    }

    /**
     * Convert a json string into a java object, especially for a collection of java objects
     * <p>
     * Example usage: fromJson(json, new TypeReference<List<T>>(){
     *         };)
     */
    public static <T> T fromJson(final String json, final TypeReference<T> collectionType) throws JsonProcessingException {
        return createObjectMapper().readValue(json, collectionType);
    }

    /**
     * Convert a json string into a java map
     */
    public static Map<String, Object> fromJson(final String json) throws JsonProcessingException {
        return createObjectMapper().readValue(json, new TypeReference<Map<String, Object>>() {
        });
    }

    /**
     * Convert a json input stream into a java object, especially for a collection of java objects
     * <p>
     * Example usage: fromJson(inputStream, new TypeReference<List<T>>(){
     *         }; )
     */
    public static <T> T fromJson(final InputStream inputStream, final TypeReference<T> collectionType) throws IOException {
        return createObjectMapper().readValue(inputStream, collectionType);
    }

    /**
     * Convert a json input stream into a java object
     */
    public static <T> T fromJson(final InputStream inputStream, final Class<T> type) throws IOException {
        return createObjectMapper().readValue(inputStream, type);
    }

    /**
     * Convert a json input stream into a java map
     */
    public Map<String, Object> fromJson(final InputStream inputStream) throws IOException {
        return createObjectMapper().readValue(inputStream, new TypeReference<Map<String, Object>>() {
        });
    }


    /**
     * Convert a java object into a json string
     */
    public static String toJson(final Object obj) throws JsonProcessingException {
        return createObjectMapper().writeValueAsString(obj);
    }

    /**
     * Deserializes the content of the given file.
     *
     * @param filename The name of the file in the classpath
     * @param type     The type of the file content
     */
    public static <T> T deserializeFileContent(final String filename, final Class<T> type) throws IOException {
        try (final FileInputResource inputResource = new FileInputResource(filename)) {
            return fromJson(inputResource.getInputStream(), type);
        } catch (final Exception e) {
            throw new IOException(
                    String.format("Error while fetching and deserializing %s at %s.", type.getName(), filename),
                    e
            );
        }
    }

    public static <T> Map<String, Object> getProperties(final T object) throws JsonProcessingException {
        return createObjectMapper().convertValue(object, new TypeReference<Map<String, Object>>() {
        });
    }


    public static ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JaxbAnnotationModule());
        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }

}
