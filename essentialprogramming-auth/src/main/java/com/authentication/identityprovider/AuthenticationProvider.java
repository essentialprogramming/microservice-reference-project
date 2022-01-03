package com.authentication.identityprovider;


import com.authentication.identityprovider.internal.entities.Account;
import com.authentication.identityprovider.internal.model.PasswordInput;
import com.authentication.identityprovider.internal.model.ResetPasswordInput;
import com.authentication.request.AuthRequest;
import com.util.password.PasswordException;
import com.util.enums.Language;
import com.util.exceptions.ApiException;

import java.io.Serializable;
import java.security.GeneralSecurityException;

public interface AuthenticationProvider {

    Account authenticate(AuthRequest authRequest, Language language) throws ApiException;
    Serializable generateOtp(String email, Language language) throws ApiException;

    Serializable resetPassword(ResetPasswordInput resetPasswordInput, Language language) throws ApiException, GeneralSecurityException;
    Serializable setPassword(PasswordInput passwordInput, Language language) throws GeneralSecurityException, ApiException, PasswordException;

}
