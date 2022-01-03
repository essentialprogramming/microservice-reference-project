package com.authentication.oauth2;

/**
 * Private claim types
 */
public enum PrivateClaims {

	MAIL("email"),

	NAME("name"),

	PLATFORM("platform"),

	ACTIVE("active"),

	ROLES("roles"),

	PERMISSIONS("permissions"),

	IDENTITY_PROVIDER("identityProvider");


	private final String loginType;

	PrivateClaims(final String loginType) {
		this.loginType = loginType;
	}

	public String getType() {
		return this.loginType;
	}
}
