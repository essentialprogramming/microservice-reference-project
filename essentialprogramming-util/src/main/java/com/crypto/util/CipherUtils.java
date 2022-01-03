package com.crypto.util;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class CipherUtils {

    public static SecretKey newSecretKey(String algorithm, PBEKeySpec keySpec) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
            return factory.generateSecret(keySpec);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalArgumentException("Not a valid encryption algorithm", exception);
        } catch (InvalidKeySpecException exception) {
            throw new IllegalArgumentException("Not a valid secret key", exception);
        }
    }
}
