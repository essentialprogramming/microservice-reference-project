package com.crypto;


import com.crypto.util.CipherUtils;
import com.crypto.util.IvGenerator;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

/**
 * Implements AES (Advanced Encryption Standard) with Galois/Counter Mode (GCM), which is a mode of
 * operation for symmetric key cryptographic block ciphers that has been widely adopted because of
 * its efficiency and performance.
 * <p>
 * Every encryption produces a new 12 byte random IV (see http://nvlpubs.nist.gov/nistpubs/Legacy/SP/nistspecialpublication800-38d.pdf)
 * because the security of GCM depends choosing a unique initialization vector for every encryption performed with the
 * same key.
 * <p>
 * Based on open source code from Patrick Favre-Bulle:
 * https://proandroiddev.com/security-best-practices-symmetric-encryption-with-aes-in-java-7616beaaade9?gi=3f161fd33990
 */
public final class Crypt {

    private static final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;

    private static final String SECRET_KEY_FACTORY_INSTANCE = "PBKDF2WithHmacSHA256";

    private static final String PREFIX = "AESGCM:";

    private Crypt() {
        throw new IllegalAccessError("Instantiation prohibited");
    }

    /**
     * Encrypts a value
     *
     * @param value to encrypt
     * @return encrypted value in base64 format
     */
    public static String encrypt(String value, String secretKey) throws GeneralSecurityException {
        if (value == null) {
            throw new IllegalArgumentException("Null input value");
        }

        if (secretKey == null || secretKey.getBytes().length < 4) {
            throw new IllegalArgumentException("Key length must be longer than 4 bytes");
        }

        final byte[] iv = IvGenerator.init(IV_LENGTH_BYTE).generateKey();
        Cipher cipher = initCipher(secretKey, Cipher.ENCRYPT_MODE, iv);

        byte[] utf8 = value.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = cipher.doFinal(utf8);

        final ByteBuffer byteBuffer = ByteBuffer.allocate(1 + iv.length + encrypted.length);
        byteBuffer.put((byte) iv.length);
        byteBuffer.put(iv);
        byteBuffer.put(encrypted);

        String base64Encrypted = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(byteBuffer.array());
        return pack(base64Encrypted);
    }

    /**
     * Decrypts an encrypted value
     *
     * @param value to decrypt
     * @return decrypted value
     */
    public static String decrypt(String value, String secretKey) throws GeneralSecurityException {
        if (value == null) {
            throw new IllegalArgumentException("Data to decrypt cannot be null");
        }

        final String unpackedData = unpack(value); //Remove prefix
        final byte[] decoded = Base64.getUrlDecoder().decode(unpackedData); // Decode base64


        final ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);

        final int ivLength = byteBuffer.get(); // first byte holds the iv length
        byte[] iv = new byte[ivLength];
        byteBuffer.get(iv); // populate iv
        byte[] encryptedData = new byte[byteBuffer.remaining()];
        byteBuffer.get(encryptedData); // populate encryptedData

        Cipher cipher = initCipher(secretKey, Cipher.DECRYPT_MODE, iv);
        byte[] decrypted = cipher.doFinal(encryptedData);

        // Decode using utf-8
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private static Cipher initCipher(String password, int encryptMode, byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        SecretKeyHolder secret = new SecretKeyHolder(password);

        PBEKeySpec keySpec = new PBEKeySpec(secret.KEY, secret.SALT, 2048, 256);
        SecretKey secretKey = CipherUtils.newSecretKey(SECRET_KEY_FACTORY_INSTANCE, keySpec);

        Key secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");


        final Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(encryptMode, secretKeySpec, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

        return cipher;
    }

    private static String pack(final String value) {
        return String.format("%s%s", PREFIX, value);
    }

    private static String unpack(final String value) {
        final String trimmedValue = value.trim();
        if (!trimmedValue.startsWith(PREFIX)) {
            throw new IllegalArgumentException(String.format("Given value doesn't respect format %s<base64encoded>", PREFIX));
        }
        return trimmedValue.substring(PREFIX.length());
    }


    private static final class SecretKeyHolder {

        private final char[] KEY;
        private final byte[] SALT;

        private SecretKeyHolder(final String secretKey) {

            final byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

            if (secretKeyBytes.length < 4) {
                throw new IllegalArgumentException("Secret key has to be at least 4 bytes length");
            }

            // if secret key is bigger than 4 bytes, then get `secretKeyBytes.length - 4` bytes-part as key; if not get first 2-bytes as key
            int keySize = secretKeyBytes.length > 4 ? secretKeyBytes.length - 4 : 2;
            byte[] keyBytes = Arrays.copyOfRange(secretKeyBytes, 0, keySize);

            KEY = Arrays.toString(keyBytes).toCharArray();
            SALT = Arrays.copyOfRange(secretKeyBytes, keySize, secretKeyBytes.length);
        }
    }
}
