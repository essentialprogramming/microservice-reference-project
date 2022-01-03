package crypto;

import com.crypto.PasswordHash;
import com.crypto.Pbkdf2PasswordEncoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Pbkdf2PasswordEncoderTest {

    private final Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = Pbkdf2PasswordEncoder.getInstance();

    @Test
    void password_should_be_equals_after_encoding() {
        String password = "password";
        String encodedPassword = pbkdf2PasswordEncoder.encode(password);

        Assertions.assertTrue(PasswordHash.matches(password, encodedPassword));
    }

    @Test
    void password_should_be_different_after_encoding() {
        String password = "Test123";
        String encodedPassword = pbkdf2PasswordEncoder.encode(password);

        Assertions.assertFalse(PasswordHash.matches("Test456", encodedPassword));
    }
}
