package com.api.service;

import com.api.entities.User;
import com.api.env.resources.AppResources;
import com.api.mapper.UserMapper;
import com.api.model.UserInput;
import com.api.output.UserJSON;
import com.api.repository.UserRepository;
import com.api.template.Templates;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.crypto.Crypt;
import com.crypto.PasswordHash;
import com.email.service.EmailManager;
import com.internationalization.EmailMessages;
import com.internationalization.Messages;
import com.util.enums.HTTPCustomStatus;
import com.util.exceptions.ApiException;
import com.util.web.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.jboss.weld.util.collections.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.List;

import static com.config.ClockConfig.UTC_CLOCK;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    private final UserRepository userRepository;
    private final EmailManager emailManager;

    @Qualifier(UTC_CLOCK)
    private final Clock clock;


    @Transactional
    public UserJSON save(UserInput input, com.util.enums.Language language) throws GeneralSecurityException {

        final User user = UserMapper.inputToUser(input);
        final User result = saveUser(user, input, language);

        LocalDateTime createdDate = LocalDateTime.now(clock);
        user.setCreatedDate(createdDate);

        String validationKey = Crypt.encrypt(NanoIdUtils.randomNanoId(), AppResources.ENCRYPTION_KEY.value());
        String encryptedUserKey = Crypt.encrypt(result.getUserKey(), AppResources.ENCRYPTION_KEY.value());

        String url = AppResources.ACCOUNT_CONFIRMATION_URL.value() + "/" + validationKey + "/" + encryptedUserKey;

        Map<String, Object> templateVariables = ImmutableMap.<String, Object>builder()
                .put("fullName", result.getFullName())
                .put("confirmationLink", url)
                .build();
        emailManager.send(result.getEmail(), EmailMessages.get("new_user.subject", language.getLocale()), Templates.NEW_USER, templateVariables, language.getLocale());

        return UserMapper.userToJson(result);

    }

    @Transactional
    public boolean checkAvailabilityByEmail(String email) {

        Optional<User> user = userRepository.findByEmail(email);
        return !user.isPresent();
    }

    @Transactional
    public boolean checkEmailExists(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }


    @Transactional
    public UserJSON loadUser(String email, com.util.enums.Language language) throws ApiException {

        final User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ApiException(Messages.get("USER.NOT.EXIST", language), HTTPCustomStatus.UNAUTHORIZED)
        );

        logger.info("User with email={} loaded", email);
        return UserMapper.userToJson(user);

    }


    private User saveUser(User user, UserInput input, com.util.enums.Language language) {

        String uuid = NanoIdUtils.randomNanoId();
        user.setUserKey(uuid);

        userRepository.save(user);
        if (user.getId() > 0) {
            logger.debug("Start password hashing");
            String password = PasswordHash.encode(input.getPassword());
            logger.debug("Finished password hashing");

            user.setPassword(password);
        }

        logger.info("user saved userId={}", user.getId());
        return user;
    }

    @Transactional
    public List<User> loadAll() {
        return userRepository.findAll();
    }

    @Transactional
    public Serializable deleteUser(final String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User with email " + email +  " not found!");
        }

        userRepository.deleteUserByEmail(email);

        return new JsonResponse()
                .with("status", "ok")
                .with("message", "User with email " + email + " was successfully deleted!")
                .done();
    }
}
