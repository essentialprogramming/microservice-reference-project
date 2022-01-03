package com.token.validation.signature;

public enum SignatureAlgorithm {

    /**
     * No digital signature
     */
    NONE("none", "No digital signature", "None", null),

    /**
     * HMAC using SHA-256
     */
    HS256("HS256", "HMAC using SHA-256", "HMAC", "HmacSHA256"),

    /**
     * HMAC using SHA-384
     */
    HS384("HS384", "HMAC using SHA-384", "HMAC", "HmacSHA384"),

    /**
     * HMAC using SHA-512
     */
    HS512("HS512", "HMAC using SHA-512", "HMAC", "HmacSHA512"),

    /**
     * RSASSA-PKCS-v1_5 using SHA-256
     */
    RS256("RS256", "RSASSA-PKCS-v1_5 using SHA-256", "RSA", "SHA256withRSA"),

    /**
     * RSASSA-PKCS-v1_5 using SHA-384
     */
    RS384("RS384", "RSASSA-PKCS-v1_5 using SHA-384", "RSA", "SHA384withRSA"),

    /**
     * RSASSA-PKCS-v1_5 using SHA-512
     */
    RS512("RS512", "RSASSA-PKCS-v1_5 using SHA-512", "RSA", "SHA512withRSA"),

    /**
     * ECDSA using P-256 and SHA-256
     */
    ES256("ES256", "ECDSA using P-256 and SHA-256", "Elliptic Curve", "SHA256withECDSA"),

    /**
     * ECDSA using P-384 and SHA-384
     */
    ES384("ES384", "ECDSA using P-384 and SHA-384", "Elliptic Curve", "SHA384withECDSA"),

    /**
     * ECDSA using P-521 and SHA-512
     */
    ES512("ES512", "ECDSA using P-512 and SHA-512", "Elliptic Curve", "SHA512withECDSA");




    private final String value;
    private final String description;
    private final String familyName;
    private final String jcaName;

    SignatureAlgorithm(String value, String description, String familyName, String jcaName) {
        this.value = value;
        this.description = description;
        this.familyName = familyName;
        this.jcaName = jcaName;
    }

    /**
     * Returns the JWA algorithm name constant.
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns the JWA algorithm description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the name of the JCA algorithm used to compute the signature.
     */
    public String getJcaName() {
        return jcaName;
    }

    public String getFamilyName() {
        return familyName;
    }


    /**
     * Returns {@code true} if the enum instance represents an HMAC signature algorithm, {@code false}
     * otherwise.
     */
    public boolean isHmac() {
        return name().startsWith("HS");
    }

    /**
     * Returns {@code true} if the enum instance represents an RSA public/private key pair signature
     * algorithm, {@code false} otherwise.
     */
    public boolean isRsa() {
        return name().startsWith("RS");
    }

    /**
     * Returns {@code true} if the enum instance represents an ECDSA signature algorithm, {@code false}
     * otherwise.
     */
    public boolean isEllipticCurve() {
        return name().startsWith("ES");
    }

    public String getSignatureType() {
        if (isHmac()) return "HMAC";
        if (isRsa())  return "RSA";
        if (isEllipticCurve())  return "ECDSA";
        return "NONE";
    }
}
