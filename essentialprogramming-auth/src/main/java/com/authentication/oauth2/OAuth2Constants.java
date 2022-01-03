package com.authentication.oauth2;


public class OAuth2Constants {

    public static final String CLIENT_ID = "client_id";

    public static final String CLIENT_SECRET = "client_secret";

    public static final String STATE = "state";

    public static final String SCOPE = "scope";

    public static final String REDIRECT_URI = "redirect_uri";

    public static final String RESPONSE_TYPE = "response_type";

    public static final String GRANT_TYPE = "grant_type";

    public static final String CODE = "code";

    public static final String TOKEN = "token";

    public static final String IMPLICIT = "implicit";

    public static final String AUTHORIZATION_CODE = "authorization_code";

    public static final String AUTHORIZATION_REQUEST = "authorization_request";

    public static final String USER_OAUTH_APPROVAL = "user_oauth_approval";

    public static final String SCOPE_PREFIX = "scope.";

    public static final String ID_TOKEN = "id_token";
    
    public static final String BEARER_TYPE = "Bearer";

    public static final String ACCESS_TOKEN = "access_token";

    public static final String TOKEN_TYPE = "token_type";

    public static final String EXPIRES_IN = "expires_in";

    public static final String REFRESH_TOKEN = "refresh_token";
    
    public final static String USERNAME = "username";
    public final static String PASSWORD = "password"; //NOSONAR

    /**
     * Next constants are defined for PKCE support.
     */
    public static final String CODE_CHALLENGE = "code_challenge";
    public static final String CODE_CHALLENGE_METHOD = "code_challenge_method";
    public static final String CODE_VERIFIER = "code_verifier";

    public static final String PKCE_METHOD_PLAIN = "plain";
    public static final String PKCE_METHOD_S256 = "S256";
}
