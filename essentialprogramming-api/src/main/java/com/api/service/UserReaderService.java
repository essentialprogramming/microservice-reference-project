package com.api.service;

import com.api.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

import static com.config.ObjectMapperConfig.ObjectMapperProvider;


@Service
@RequiredArgsConstructor
public class UserReaderService {

    private final ObjectMapperProvider objectMapperProvider;

    public User readUser(InputStream inputStream) throws IOException {
        return objectMapperProvider.getObjectMapper().readerFor(User.class).readValue(inputStream);
    }
}
