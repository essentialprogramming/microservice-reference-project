package com.util.convertor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.util.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class JacksonEnumConverter implements GenericConverter {

    private final ObjectMapper mapper;
    private final Set<ConvertiblePair> set;

    @Autowired
    public JacksonEnumConverter() {
        set = new HashSet<>();
        set.add(new ConvertiblePair(String.class, Enum.class));
        this.mapper = JsonUtil.createObjectMapper();
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return set;
    }

    @Override
    public Object convert(@Nullable Object source, @NonNull TypeDescriptor sourceType, @NonNull TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        try {
            return mapper.readValue("\"" + source + "\"", targetType.getType());
        } catch (IOException e) {
            throw new IllegalArgumentException (targetType.getName());
        }
    }

}
