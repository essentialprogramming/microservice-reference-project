package com.authentication.security;

import com.util.exceptions.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PemUtilsTest {


    @Test
    void get_public_key_from_PEM_successfully() throws NoSuchAlgorithmException, IOException {
        final String publicKeyPem = createPublicKeyPem();
        File pemFile = File.createTempFile("publicKey", "pem", null);
        FileOutputStream fileOutputStream = new FileOutputStream(pemFile);
        fileOutputStream.write(publicKeyPem.getBytes());

        Assertions.assertNotNull(PemUtils.readPublicKeyFromPEMFile(pemFile.getPath()));
    }

    @Test
    void get_invalid_public_key_from_PEM_throws_error() throws IOException {
        final String publicKeyPem = "invalid public key";
        File pemFile = File.createTempFile("publicKey", "pem", null);
        FileOutputStream fileOutputStream = new FileOutputStream(pemFile);
        fileOutputStream.write(publicKeyPem.getBytes());

        Exception exception = assertThrows(ServiceException.class, () -> PemUtils.readPublicKeyFromPEMFile(pemFile.getPath()));
        Assertions.assertTrue(exception.getMessage().contains("Error trying to load public key from PEM format"));
    }

    @Test
    void get_private_key_from_PEM_successfully() throws NoSuchAlgorithmException, IOException {
        final String privateKeyPem = createPrivateKeyPem();
        File pemFile = File.createTempFile("privateKey", "pem", null);
        FileOutputStream fileOutputStream = new FileOutputStream(pemFile);
        fileOutputStream.write(privateKeyPem.getBytes());

        Assertions.assertNotNull(PemUtils.readPrivateKeyFromPEMFile(pemFile.getPath()));
    }

    @Test
    void get_invalid_private_key_from_PEM_throws_error() throws IOException {
        final String privateKeyPem = "invalid private key";
        File pemFile = File.createTempFile("privateKey", "pem", null);
        FileOutputStream fileOutputStream = new FileOutputStream(pemFile);
        fileOutputStream.write(privateKeyPem.getBytes());

        Exception exception = assertThrows(ServiceException.class, () -> PemUtils.readPrivateKeyFromPEMFile(pemFile.getPath()));
        Assertions.assertTrue(exception.getMessage().contains("Error trying to load private key from PEM format"));
    }

    private String createPublicKeyPem() throws NoSuchAlgorithmException {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        String publicKeyText = "-----BEGIN RSA PUBLIC KEY-----\n";
        publicKeyText +=
                Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(keyPair.getPublic().getEncoded());
        publicKeyText += "\n-----END RSA PUBLIC KEY-----";
        return publicKeyText;
    }

    private String createPrivateKeyPem() throws NoSuchAlgorithmException {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        String privateKeyText = "-----BEGIN RSA PRIVATE KEY-----\n";
        privateKeyText += Base64.getMimeEncoder(64, "\n".getBytes())
                .encodeToString(keyPair.getPrivate().getEncoded());
        privateKeyText += "\n-----END RSA PRIVATE KEY-----";
        return privateKeyText;
    }
}
