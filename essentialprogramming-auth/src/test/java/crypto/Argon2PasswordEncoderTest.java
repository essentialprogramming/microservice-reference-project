package crypto;

import com.crypto.Argon2PasswordEncoder;
import com.crypto.PasswordHash;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Argon2PasswordEncoderTest {

    private final Argon2PasswordEncoder argon2PasswordEncoder = Argon2PasswordEncoder.getInstance();

    @Test
    void password_should_be_equals_after_encoding() {
        String password = "password";
        String encodedPassword = argon2PasswordEncoder.encode(password);
        Assertions.assertTrue(PasswordHash.matches(password, encodedPassword));
    }

    @Test
    void password_should_be_different_after_encoding() {
        String password = "Test123";
        String encodedPassword = argon2PasswordEncoder.encode(password);

        Assertions.assertFalse(PasswordHash.matches("Test456", encodedPassword));
    }

}
