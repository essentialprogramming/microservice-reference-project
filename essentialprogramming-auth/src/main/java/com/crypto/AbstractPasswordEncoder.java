package com.crypto;

import com.authentication.exceptions.codes.ErrorCode;
import com.util.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public abstract class AbstractPasswordEncoder implements PasswordEncoder {

    final static Logger logger = LoggerFactory.getLogger(AbstractPasswordEncoder.class);

    public String encode(CharSequence passwordSequence ) {
        logger.debug("Encoding password");
        String password = passwordSequence.toString();
        try {
            return createHash(password.toCharArray());
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException e) {
            logger.error(ErrorCode.PASSWORD_HASH_CREATION_NOT_SUCCESSFUL.getDescription(), e);
            throw new ServiceException(ErrorCode.PASSWORD_HASH_CREATION_NOT_SUCCESSFUL);
        }
    }

    /**
     * Verify that the encoded password obtained from storage matches the submitted raw
     * password after the raw password is also encoded. Returns true if the passwords match, false if
     * they do not. The stored password itself is never decoded.
     *
     * @param passwordSequence               the password to check
     * @param encodedPassword the stored hashed password with salt
     * @return true if the password is correct, false if not
     */
    @Override
    public boolean matches(CharSequence passwordSequence, String encodedPassword) {
        String password = passwordSequence.toString();
        String[] parts = encodedPassword.split(":");
        if (parts.length < 2) {
            return false;
        }
        byte[] hash = EncodingUtils.fromHex(parts[0]);
        byte[] salt = EncodingUtils.fromHex(parts[1]);


        return matches(password, hash, salt);
    }



    /**
     * Compare passwords
     *
     * @param password the password to check
     * @param hash     hashed password
     * @param salt     Salt used to hash password
     * @return true if the password is correct, false if not
     */
    boolean matches(String password, byte[] hash, byte[] salt) {
        boolean result;
        try {
            result = matches(password.toCharArray(), hash, salt);
        } catch (Exception e) {
            logger.error(ErrorCode.PASSWORD_HASH_CREATION_NOT_SUCCESSFUL.getDescription(), e);
            throw new ServiceException(ErrorCode.PASSWORD_HASH_CREATION_NOT_SUCCESSFUL);
        }

        return result;
    }

    abstract boolean matches(char[] password, byte[] hash, byte[] salt) throws Exception;
    abstract String createHash(char[] password) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException;


}
