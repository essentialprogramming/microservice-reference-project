package com.authentication.identityprovider.internal.service;

import com.authentication.identityprovider.internal.entities.*;
import com.authentication.identityprovider.AuthenticationProvider;
import com.authentication.identityprovider.internal.model.PasswordInput;
import com.authentication.identityprovider.internal.model.ResetPasswordInput;
import com.authentication.identityprovider.internal.repository.*;
import com.authentication.request.AuthRequest;
import com.authentication.template.TemplateEnum;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.crypto.PasswordHash;
import com.email.service.EmailManager;
import com.util.password.PasswordException;
import com.internationalization.EmailMessages;
import com.internationalization.Messages;
import com.util.enums.HTTPCustomStatus;
import com.util.enums.Language;
import com.util.exceptions.ApiException;
import com.util.password.PasswordStrength;
import com.util.password.PasswordUtil;
import com.util.web.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

import static com.resources.AppResources.OTP_LOGIN_URL;


@Service
public class AccountService implements AuthenticationProvider {

    private final AccountRepository accountRepository;
    private final EmailManager emailManager;

    @Autowired
    public AccountService(AccountRepository accountRepository, EmailManager emailManager) {
        this.accountRepository = accountRepository;
        this.emailManager = emailManager;
    }

    /**
     * Authenticate with username and password .
     *
     * @param authRequest AuthRequest
     * @return Account for the username after authentication.
     */
    @Override
    public Account authenticate(AuthRequest authRequest, Language language) throws ApiException {
        Optional<Account> account = getAccount(authRequest.getEmail());

        if (!account.isPresent()) {
            throw new ApiException(Messages.get("USER.NOT.EXIST", Language.ENGLISH), HTTPCustomStatus.UNAUTHORIZED);
        }
        if (account.get().isDeleted()) {
            throw new ApiException(Messages.get("USER.ACCOUNT.DELETED", language), HTTPCustomStatus.UNAUTHORIZED);
        }

        boolean isValidPassword = PasswordHash.matches(authRequest.getPassword(), account.get().getPassword());

        if (isValidPassword) {
            return account.get();
        }
        throw new ApiException(Messages.get("USER.PASSWORD.INVALID", language), HTTPCustomStatus.UNAUTHORIZED);
    }

    private Optional<Account> getAccount(String email) {
        return accountRepository.findByEmail(email);
    }

    public Serializable setPassword(PasswordInput passwordInput, Language language) throws ApiException, PasswordException {
        Optional<Account> account = accountRepository.findByUserKey(passwordInput.getKey());

        if (!account.isPresent()) {
            throw new ApiException(Messages.get("USER.NOT.EXIST", Language.ENGLISH), HTTPCustomStatus.UNAUTHORIZED);
        }
        if (account.get().isDeleted()) {
            throw new ApiException(Messages.get("USER.ACCOUNT.DELETED", language), HTTPCustomStatus.UNAUTHORIZED);
        }

        if (!passwordInput.getNewPassword().equals(passwordInput.getConfirmPassword())) {
            throw new ApiException(Messages.get("USER.PASSWORD.DONT.MATCH", language), HTTPCustomStatus.INVALID_REQUEST);
        }

        PasswordStrength passwordPower = PasswordUtil.getPasswordStrength(passwordInput.getNewPassword());
        boolean passwordStrength = PasswordUtil.isStrongPassword(passwordInput.getNewPassword());

        if (!passwordStrength)
            throw new PasswordException(Messages.get("USER.PASSWORD.STRENGTH", language), PasswordStrength.get(passwordPower.getValue()));

        account.ifPresent(user -> user.setPassword(PasswordHash.encode(passwordInput.getNewPassword())));


        return new JsonResponse()
                .with("status", "ok")
                .done();
    }


    public Serializable generateOtp(String email, Language language) throws ApiException {
        String otp = NanoIdUtils.randomNanoId();

        Account account = accountRepository.findByEmail(email).orElseThrow(() ->
                new ApiException(Messages.get("USER.NOT.EXIST", language), HTTPCustomStatus.UNAUTHORIZED));


        String url = OTP_LOGIN_URL.value() + "?email=" + account.getEmail() + "&otp=" + otp;

        Map<String, Object> templateKeysAndValues = new HashMap<>();
        templateKeysAndValues.put("fullName", account.getFullName());
        templateKeysAndValues.put("link", url);
        emailManager.send(account.getEmail(), EmailMessages.get("otp_login.subject", language.getLocale()), TemplateEnum.OTP_LOGIN, templateKeysAndValues, language.getLocale());

        return new JsonResponse()
                .with("status", "ok")
                .done();
    }

    @Override
    public Serializable resetPassword(ResetPasswordInput resetPasswordInput, Language language) throws ApiException {
        return null;
    }
}
