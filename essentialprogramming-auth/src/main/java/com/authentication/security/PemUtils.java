package com.authentication.security;

import com.authentication.exceptions.codes.ErrorCode;
import com.util.exceptions.ServiceException;
import com.util.io.FileInputResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class PemUtils {


    /**
     * Return Public Key loaded from PEM key format
     *
     * @param key public key in PEM format
     * @return Public Key
     */
    private static PublicKey getPublicKey(String key) {

        try {
            // Parse and remove header, footer, newlines and whitespaces
            final String publicKeyPEM = PemUtils.sanitizePublicPEM(key);
            final byte[] keyBytes = Base64.getDecoder().decode(publicKeyPEM);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.UNABLE_TO_GET_PUBLIC_KEY,
                    "Error trying to load public key from PEM format");
        }
    }


    /**
     * Return Private Key loaded from PEM key format
     *
     * @param key private Key in PEM format
     * @return privateKey
     */
    private static PrivateKey getPrivateKey(String key) {

        try {
            // Parse and remove header, footer, newlines and whitespaces
            String privateKeyPEM = PemUtils.sanitizePrivatePEM(key);

            byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.UNABLE_TO_GET_PRIVATE_KEY,
                    "Error trying to load private key from PEM format");
        }
    }

    private static byte[] parsePEMFile(String pemFile) throws IOException {
        FileInputResource fileInputResource = new FileInputResource(pemFile);
        return fileInputResource.getBytes();
    }

    public static PublicKey readPublicKeyFromPEMFile(String pemFile) throws IOException {
        byte[] bytes = PemUtils.parsePEMFile(pemFile);
        return PemUtils.getPublicKey(new String(bytes, StandardCharsets.UTF_8));
    }

    public static PrivateKey readPrivateKeyFromPEMFile(String pemFile) throws IOException {
        byte[] bytes = PemUtils.parsePEMFile(pemFile);
        return PemUtils.getPrivateKey(new String(bytes, StandardCharsets.UTF_8));
    }

    private static String sanitizePublicPEM(String publicKeyContent) {
        return publicKeyContent
                .replace("-----BEGIN RSA PUBLIC KEY-----", "")
                .replace("-----END RSA PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
    }

    private static String sanitizePrivatePEM(String privateKeyContent ) {
        return privateKeyContent
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

    }
}
