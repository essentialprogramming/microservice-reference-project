package com.token.validation.crypto;

import com.token.validation.jwt.Jwt;
import com.token.validation.jwt.exception.SignatureVerificationException;
import com.token.validation.signature.SignatureAlgorithm;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HMACProvider implements SignatureVerifier {

    private HMACProvider() {
    }

    private static class HMACProviderHolder {
        static final HMACProvider INSTANCE = new HMACProvider();
    }

    public static HMACProvider getInstance() {
        return HMACProviderHolder.INSTANCE;
    }


    public boolean verify(final Jwt input, final Key key) throws SignatureVerificationException {
        return verify(input, (SecretKey) key);
    }

    private static boolean verify(Jwt input, SecretKey key) throws SignatureVerificationException {
        try {
            byte[] signature = sign((input.getEncodedHeader() + '.' + input.getEncodedContent()).getBytes(StandardCharsets.UTF_8),
                            input.getHeader().getAlgorithm(), key);
            return MessageDigest.isEqual(signature, input.getSignature());
        } catch (Exception e) {
            throw new SignatureVerificationException("Something went wrong on signature verification");
        }
    }


    private static byte[] sign(byte[] data, SignatureAlgorithm algorithm, SecretKey key) throws InvalidKeyException, NoSuchAlgorithmException {
        final Mac mac = Mac.getInstance(getJavaCryptographicAlgorithm(algorithm));
        mac.init(key);
        mac.update(data);
        return mac.doFinal();
    }

    private static String getJavaCryptographicAlgorithm(SignatureAlgorithm algorithm) {
        if (!algorithm.isHmac())
            throw new IllegalArgumentException("Not a MAC Algorithm");
        return algorithm.getJcaName();
    }

}
