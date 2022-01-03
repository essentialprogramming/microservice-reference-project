package com.authentication.identityprovider;

public final class IdentityProviders {

  private IdentityProviders() {
    throw new IllegalStateException("Utility class");
  }

  public static final String KEYCLOAK = "keycloak";
  public static final String AUTH0 = "auth0";
  public static final String MICRO_SERVICE_REFERENCE_PROJECT = "essential~programming";

}
