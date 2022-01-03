package com.authentication.oauth2;

import java.util.Arrays;
import java.util.List;

/**
 * Claim Names
 *
 */
public interface Claims {

    /**
     * The "iss" (issuer) claim identifies the principal that issued the JWT.
     */
    String ISS = "iss";

    /**
     * The "sub" (subject) claim identifies the principal that is the subject of the JWT.
     */
    String SUB = "sub";

    /**
     * The "aud" (audience) claim identifies the recipients that the JWT is intended for.
     */
    String AUD = "aud";

    /**
     * The "exp" (expiration time) claim identifies the expiration time on or after which the JWT MUST NOT be accepted for processing.
     */
    String EXP = "exp";

    /**
     * The "nbf" (not before) claim identifies the time before which the JWT MUST NOT be accepted for processing.
     */
    String NBF = "nbf";

    /**
     * The "iat" (issued at) claim identifies the time at which the JWT was issued.
     */
    String IAT = "iat";

    /**
     * The "jti" (JWT ID) claim provides a unique identifier for the JWT.
     */
    String JTI = "jti";

    static List<String> claims() {
        return Arrays.asList(SUB, ISS, AUD, EXP, NBF, IAT, JTI);
    }
}
