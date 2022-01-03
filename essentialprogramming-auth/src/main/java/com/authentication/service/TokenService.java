package com.authentication.service;

import com.authentication.exceptions.codes.ErrorCode;
import com.authentication.security.KeyStoreService;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import com.util.exceptions.ServiceException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.AESEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.util.cloud.Configuration;
import com.util.cloud.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
public class TokenService {
    private static final Logger LOG = LoggerFactory.getLogger(TokenService.class);

    //Always RSA 256, but could be parametrized
    private final JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256)
            .type(JOSEObjectType.JWT)
            .build();

    private final KeyStoreService keyStoreService;

    private final Configuration configuration = ConfigurationManager.getConfiguration();


    @Autowired
    public TokenService(KeyStoreService keyStoreService) {
        this.keyStoreService = keyStoreService;
    }


    /**
     * Generates an oauth token and returns it.
     * <p>
     * The payload is transported as a signed nested JWT (integrity).
     * This nested JWT in turn is the payload of an encrypted JWT (confidentiality).
     * 1. Create the nested JWT
     * Create JWT with payload (clear text, token payload)
     * Sign JWT using JWS
     * 2. Create encrypted JWT
     * Encrypt nested JWT including the JWS signature to create a new JWT
     *
     * @param privateClaimTypeAndString      The map of private claims where the values are single strings
     * @param privateClaimTypeAndStringArray The map of private claims where the values are string arrays
     * @return The oauth token
     * @throws ServiceException The ServiceException
     */
    public String generateJwtToken(final long expiresIn,
                                   final Map<String, String> privateClaimTypeAndString,
                                   final Map<String, String[]> privateClaimTypeAndStringArray)
            throws ServiceException {
        LOG.info("Creating signed JWT token");

        // Create the payload
        Payload payload = createTokenPayload(expiresIn, privateClaimTypeAndString,
                privateClaimTypeAndStringArray);


        // Create the JWT containing the payload
        // Sign the JWT to create the JWS token
        // Result: nested signed token containing the payload

        return createSignedToken(payload, keyStoreService.getPrivateKey());

        // Encrypt the JWS token
        // Create the encrypted JWE token containing the nested token
        // Result: JWE Its only content is the signed  token, this token string is delivered to the client
        //LOG.info("encrypting JWS token and creating encrypted JWE token");
        //return encryptToken(signedJws, keyStoreService.getSymmetricKey());
    }


    private Payload createTokenPayload(long expiresIn,
                                       Map<String, String> privateClaimTypeAndString,
                                       Map<String, String[]> privateClaimTypeAndStringArray) {

        Instant now = Instant.now();
        Date expirationTime = Date.from(now.plus(expiresIn, ChronoUnit.MINUTES));

        //3. JWT Payload or claims
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .issuer("Essential Programming Auth Service")
                .audience("Essential Programming Services")
                .expirationTime(expirationTime) // expires in 30 minutes
                .notBeforeTime(Date.from(now))
                .issueTime(Date.from(now))
                .jwtID(NanoIdUtils.randomNanoId());

        if (privateClaimTypeAndString != null) {
            for (Map.Entry<String, String> entry : privateClaimTypeAndString.entrySet()) {
                builder.claim(entry.getKey(), entry.getValue());
            }
        }

        if (privateClaimTypeAndStringArray != null) {
            for (Map.Entry<String, String[]> entry : privateClaimTypeAndStringArray.entrySet()) {
                builder.claim(entry.getKey(), entry.getValue());
            }
        }

        JWTClaimsSet jwtClaims = builder.build();
        // return payload
        return new Payload(jwtClaims.toJSONObject());
    }


    private String createSignedToken(Payload payload, PrivateKey privateSigningKey)
            throws ServiceException {

        if (privateSigningKey == null) {
            throw new ServiceException(ErrorCode.PRIVATE_KEY_IS_NULL);
        }

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            JWSSigner signer = new RSASSASigner(privateSigningKey, false);
            jwsObject.sign(signer);
        } catch (JOSEException e) {
            LOG.error(ErrorCode.UNABLE_TO_SIGN_TOKEN.getDescription());
            throw new ServiceException(ErrorCode.UNABLE_TO_SIGN_TOKEN, e);
        }

        return jwsObject.serialize();
    }

    private String encryptToken(String nestedJwt,
                                byte[] encodedKey)
            throws ServiceException {
        // create JWE header
        JWEHeader header = new JWEHeader.Builder(JWEAlgorithm.A256KW, EncryptionMethod.A256CBC_HS512).contentType("JWT")
                .build();

        // create payload
        Payload payload = new Payload(nestedJwt);

        // encrypt
        JWEObject jweObject = new JWEObject(header, payload);

        try {
            jweObject.encrypt(new AESEncrypter(encodedKey));
        } catch (JOSEException e) {
            LOG.error(ErrorCode.UNABLE_TO_ENCRYPT_TOKEN.getDescription());
            throw new ServiceException(ErrorCode.UNABLE_TO_ENCRYPT_TOKEN, e);
        } catch (IllegalArgumentException e) {
            LOG.error(ErrorCode.SYMMETRIC_KEY_IS_NULL.getDescription());
            throw new ServiceException(ErrorCode.SYMMETRIC_KEY_IS_NULL, e);
        }

        return jweObject.serialize();
    }

    /**
     * Validates a token (SSO token to authenticate the user)
     * There are several steps in validation:
     * 1. Decrypt JWE Payload
     * 2. Check Signature of nested JWT
     * 3. Check if the expiration date (exp) is in the future
     * 4. Check if the token is valid for the specific hub (iss)
     *
     * @param encryptedJwe token (encrypted JWE)
     * @throws ServiceException the authentication fails when the token is not valid
     */
    public void verifyToken(String encryptedJwe)
            throws ServiceException {

        // Decrypt the JWE token and parse it
        // Retrieve signed JWS Token
        // Verify the signature of the nested JWS token and parse it
    }

    protected JWSSigner getJwsSigner() throws Exception {
        String signingKey = configuration.getPropertyAsString("signingKey");
        String pemEncodedRSAPrivateKey = signingKey;
        RSAKey rsaKey = (RSAKey) JWK.parseFromPEMEncodedObjects(pemEncodedRSAPrivateKey);
        return new RSASSASigner(rsaKey.toRSAPrivateKey());
    }

    protected String getRefreshToken(String email) {
        JWSSigner jwsSigner = new RSASSASigner(keyStoreService.getPrivateKey(), true);
        Instant now = Instant.now();

        JWTClaimsSet refreshTokenClaims = new JWTClaimsSet.Builder()
                .issuer("EssentialProgramming Auth Service")
                .claim("email", email)
                //refresh token for 1 day.
                .expirationTime(Date.from(now.plus(1, ChronoUnit.DAYS)))
                .build();
        SignedJWT signedRefreshToken = new SignedJWT(jwsHeader, refreshTokenClaims);
        try {
            signedRefreshToken.sign(jwsSigner);
        } catch (JOSEException e) {
            LOG.error(ErrorCode.UNABLE_TO_SIGN_TOKEN.getDescription());
            throw new ServiceException(ErrorCode.UNABLE_TO_SIGN_TOKEN, e);
        }
        return signedRefreshToken.serialize();
    }


}