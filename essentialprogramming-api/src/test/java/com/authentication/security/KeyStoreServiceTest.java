package com.authentication.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyStoreServiceTest {

    KeyStoreService keyStoreService = new KeyStoreService();


    @Test
    void get_public_key() {
        PublicKey publicKey = keyStoreService.getPublicKey();

        Assertions.assertNotNull(publicKey);
        Assertions.assertEquals("RSA", publicKey.getAlgorithm());
    }

    @Test
    void get_private_key() {
        PrivateKey privateKey = keyStoreService.getPrivateKey();

        Assertions.assertNotNull(privateKey);
        Assertions.assertEquals("RSA", privateKey.getAlgorithm());
    }
}
