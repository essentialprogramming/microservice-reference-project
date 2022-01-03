package com.authentication.service;


import com.authentication.channel.AccessChannel;
import com.authentication.oauth2.PrivateClaims;
import com.authentication.identityprovider.AuthenticationProvider;
import com.authentication.identityprovider.IdentityProviders;
import com.authentication.identityprovider.internal.entities.Account;
import com.authentication.exceptions.codes.ErrorCode;
import com.authentication.identityprovider.internal.model.ResetPasswordInput;
import com.authentication.identityprovider.internal.repository.AccountRepository;
import com.authentication.identityprovider.internal.service.AccountService;
import com.authentication.identityprovider.internal.model.PasswordInput;
import com.authentication.oauth2.OAuth2Constants;
import com.authentication.request.TokenRequest;
import com.util.date.DateUtil;
import com.util.password.PasswordException;
import com.internationalization.Messages;
import com.util.enums.HTTPCustomStatus;
import com.util.enums.Language;
import com.util.exceptions.ApiException;
import com.util.exceptions.ServiceException;
import com.authentication.request.AuthRequest;
import com.authentication.response.AccessToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
public class AuthenticationService {

    private final AuthenticationProvider accountService;
    private final TokenService tokenService;
    private final AccountRepository accountRepository;

    @Autowired
    public AuthenticationService(AccountService accountService, TokenService tokenService,
                                 AccountRepository accountRepository) {
        this.accountService = accountService;
        this.tokenService = tokenService;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccessToken authenticate(TokenRequest authRequest, AccessChannel accessChannel, Language language) throws ApiException {
        AccessToken accessTokenResponse;
        switch (accessChannel) {
            case PASSWORD:
                accessTokenResponse = loginWithPassword((AuthRequest) authRequest, language);
                break;
            case OTP:
                accessTokenResponse = otpLogin((AuthRequest) authRequest, language);
                break;
            default:
                throw new ServiceException(ErrorCode.UNDEFINED_ACCESS_CHANNEL);
        }

        return accessTokenResponse;
    }

    private AccessToken loginWithPassword(AuthRequest authRequest, Language language) throws ApiException {
        if (StringUtils.isEmpty(authRequest.getEmail())) {
            throw new ApiException(Messages.get("USERNAME.MANDATORY", language), HTTPCustomStatus.INVALID_REQUEST);
        }
        if (StringUtils.isEmpty(authRequest.getPassword())) {
            throw new ApiException(Messages.get("PASSWORD.MANDATORY", language), HTTPCustomStatus.INVALID_REQUEST);
        }

        Account account = accountService.authenticate(authRequest, language);
        Map<String, String> privateClaimMap =
                privateClaims(account.getEmail(), account.isActive(), Collections.singletonList("visitor"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plus(30, ChronoUnit.DAYS);

        return getAccessToken(now, expirationTime, privateClaimMap, permissionsClaim(), account.isActive(), account.getEmail());
    }


    private static Map<String, String> privateClaims(String email, boolean active, List<String> roles) {
        Map<String, String> privateClaimMap = new HashMap<>();
        if (email != null) {
            privateClaimMap.put(PrivateClaims.MAIL.getType(), email);
        }

        if (roles != null && !roles.isEmpty()) {
            privateClaimMap.put(PrivateClaims.ROLES.getType(), String.join(",", roles));
        }

        privateClaimMap.put(PrivateClaims.ACTIVE.getType(), String.valueOf(active));
        privateClaimMap.put(PrivateClaims.IDENTITY_PROVIDER.getType(), IdentityProviders.MICRO_SERVICE_REFERENCE_PROJECT);

        return privateClaimMap;
    }

    private static Map<String, String[]> permissionsClaim() {
        Map<String, String[]> privateClaimListMap = new HashMap<>();
        privateClaimListMap.put(PrivateClaims.PERMISSIONS.getType(), Collections.singletonList("read:user").toArray(new String[0]));

        return privateClaimListMap;
    }

    private AccessToken getAccessToken(LocalDateTime now, LocalDateTime expirationTime, Map<String, String> privateClaimMap, Map<String, String[]> privateClaimTypeAndStringArray, boolean active, String email) {
        long expiresIn = ChronoUnit.MINUTES.between(now, expirationTime);
        return AccessToken.builder()
                .accessToken(tokenService.generateJwtToken(expiresIn, privateClaimMap, privateClaimTypeAndStringArray))
                .tokenType(OAuth2Constants.BEARER_TYPE)
                .active(active)
                .refreshToken(tokenService.getRefreshToken(email))
                .expireAt(DateUtil.convertToDateViaSqlDate(expirationTime.toLocalDate()))
                .expiresIn(Period.between(now.toLocalDate(), expirationTime.toLocalDate()).getDays())
                .expiresIn(ChronoUnit.SECONDS.between(now, expirationTime))
                .build();
    }


    @Transactional
    public Serializable setPassword(PasswordInput passwordInput, Language language) throws GeneralSecurityException, ApiException, PasswordException {
        return accountService.setPassword(passwordInput, language);
    }

    @Transactional
    public Serializable resetPassword(ResetPasswordInput resetPasswordInput, Language language) throws ApiException, GeneralSecurityException {
        return accountService.resetPassword(resetPasswordInput, language);
    }

    private AccessToken otpLogin(AuthRequest authRequest, Language language) throws ApiException {

        Account account = accountRepository.findByEmail(authRequest.getEmail()).orElseThrow(() ->
                new ApiException(Messages.get("USER.NOT.EXIST", language), HTTPCustomStatus.UNAUTHORIZED)
        );


        Map<String, String> privateClaimMap =
                privateClaims(account.getEmail(), account.isActive(), null);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plus(30, ChronoUnit.DAYS);

        return getAccessToken(now, expirationTime, privateClaimMap, permissionsClaim(), account.isActive(), account.getEmail());
    }

    @Transactional
    public Serializable generateOtp(String email, Language language) throws ApiException {
        return accountService.generateOtp(email, language);
    }

}
