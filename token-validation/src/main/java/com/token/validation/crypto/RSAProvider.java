package com.token.validation.crypto;

import com.token.validation.jwt.Jwt;
import com.token.validation.jwt.exception.SignatureVerificationException;
import com.token.validation.signature.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;

public class RSAProvider implements SignatureVerifier {

    private RSAProvider() {
    }

    private static class RSASProviderHolder {
        static final RSAProvider INSTANCE = new RSAProvider();
    }

    public static RSAProvider getInstance() {
        return RSASProviderHolder.INSTANCE;
    }



    public boolean verify(final Jwt input, final Key key) throws SignatureVerificationException {
        return verify(input, (PublicKey) key);
    }

    private static boolean verify(Jwt input, PublicKey publicKey) throws SignatureVerificationException {
        try {
            Signature verifier = getSignature(input.getHeader().getAlgorithm());
            verifier.initVerify(publicKey);
            verifier.update((input.getEncodedHeader() + '.' + input.getEncodedContent()).getBytes(StandardCharsets.UTF_8));
            return verifier.verify(input.getSignature());
        } catch (Exception e) {
            throw new SignatureVerificationException("Something went wrong on signature verification", e);
        }
    }

    private static Signature getSignature(SignatureAlgorithm alg) throws NoSuchAlgorithmException {
        return Signature.getInstance(getJavaCryptographicAlgorithm(alg));
    }

    private static String getJavaCryptographicAlgorithm(SignatureAlgorithm algorithm) {
        if (!algorithm.isRsa())
            throw new IllegalArgumentException("Not an RSA Algorithm");
        return algorithm.getJcaName();
    }


}
