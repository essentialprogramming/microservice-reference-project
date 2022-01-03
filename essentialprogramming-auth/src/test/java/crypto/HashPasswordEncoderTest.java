package crypto;

import com.crypto.HashPasswordEncoder;
import com.crypto.PasswordHash;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HashPasswordEncoderTest {

    private final HashPasswordEncoder hashPasswordEncoder = HashPasswordEncoder.getInstance();

    @Test
    void password_should_be_equals_after_encoding() {
        String password = "password";
        String encodedPassword = hashPasswordEncoder.encode(password);
        System.out.println(encodedPassword);
        Assertions.assertTrue(PasswordHash.matches(password, encodedPassword));
    }

    @Test
    void password_should_be_different_after_encoding() {
        String password = "Test123";
        String encodedPassword = hashPasswordEncoder.encode(password);

        Assertions.assertFalse(hashPasswordEncoder.matches("Test456", encodedPassword));
    }
}
