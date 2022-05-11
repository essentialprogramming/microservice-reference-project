package com.api.service;

import com.api.entities.User;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static com.config.ObjectMapperConfig.ObjectMapperProvider;


@Service
@RequiredArgsConstructor
public class UserReaderService {

    private final ObjectMapperProvider objectMapperProvider;

    public User readUser(final InputStream inputStream) throws IOException {
        return objectMapperProvider.getObjectMapper().readerFor(User.class).readValue(inputStream);
    }

    public Map<String, Object> read(final InputStream inputStream) throws IOException {
        return objectMapperProvider.getObjectMapper().readValue(inputStream, new TypeReference<Map<String, Object>>() {
        });
    }

    public <T> T read(final InputStream inputStream, final Class<T> type) throws IOException {
        return objectMapperProvider.getObjectMapper().readValue(inputStream, type);
    }
}
