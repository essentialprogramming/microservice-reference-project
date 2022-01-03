package com.token.validation.crypto;

import com.token.validation.jwt.Jwt;
import com.token.validation.jwt.exception.SignatureVerificationException;
import com.token.validation.signature.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.ECPublicKey;
import java.util.HashMap;
import java.util.Map;

public class EllipticCurveProvider implements SignatureVerifier {

    public static final Map<SignatureAlgorithm, String> ELLIPTIC_CURVE_NAMES = createEcCurveNames();

    private EllipticCurveProvider() {
    }

    private static class EllipticCurveProviderHolder {
        static final EllipticCurveProvider INSTANCE = new EllipticCurveProvider();
    }

    public static EllipticCurveProvider getInstance() {
        return EllipticCurveProviderHolder.INSTANCE;
    }


    public boolean verify(final Jwt input, final Key key) throws SignatureVerificationException {
        return verify(input, (PublicKey) key);
    }

    private static boolean verify(Jwt jwt, PublicKey publicKey) throws SignatureVerificationException {
        if (!(publicKey instanceof ECPublicKey))
            throw new IllegalArgumentException("Elliptic Curve signature validation requires an ECPublicKey instance");
        try {
            final Signature verifier = getSignature(jwt.getHeader().getAlgorithm());
            verifier.initVerify(publicKey);
            verifier.update((jwt.getEncodedHeader() + '.' + jwt.getEncodedContent()).getBytes(StandardCharsets.UTF_8));

            return verifier.verify(jwt.getSignature());
        } catch (Exception exception) {
            throw new SignatureVerificationException("Something went wrong on signature verification", exception);
        }
    }

    private static Signature getSignature(SignatureAlgorithm alg) throws NoSuchAlgorithmException {
        return Signature.getInstance(getJavaCryptographicAlgorithm(alg));
    }

    private static String getJavaCryptographicAlgorithm(SignatureAlgorithm algorithm) {
        if (!algorithm.isEllipticCurve())
            throw new IllegalArgumentException("Not an Elliptic Curve Algorithm");
        return algorithm.getJcaName();
    }

    private static Map<SignatureAlgorithm, String> createEcCurveNames() {
        final Map<SignatureAlgorithm, String> ecCurveNames = new HashMap<>();
        ecCurveNames.put(SignatureAlgorithm.ES256, "secp256r1");
        ecCurveNames.put(SignatureAlgorithm.ES384, "secp384r1");
        ecCurveNames.put(SignatureAlgorithm.ES512, "secp521r1");
        return ecCurveNames;
    }
}