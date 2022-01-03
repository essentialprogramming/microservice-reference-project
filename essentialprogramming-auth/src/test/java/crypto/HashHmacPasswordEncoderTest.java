package crypto;

import com.crypto.HashHmacPasswordEncoder;
import com.crypto.PasswordHash;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class HashHmacPasswordEncoderTest {

    private final HashHmacPasswordEncoder hashHmacPasswordEncoder = HashHmacPasswordEncoder.getInstance();

    @BeforeAll
    static void init(){
        System.setProperty(HashHmacPasswordEncoder.SECRET_KEY, "secret");
    }
    @Test
    void password_should_be_equals_after_encoding() {
        String password = "password";
        String encodedPassword = hashHmacPasswordEncoder.encode(password);
        Assertions.assertTrue(PasswordHash.matches(password, encodedPassword));
    }

    @Test
    void password_should_be_different_after_encoding() {
        String password = "Test123";
        String encodedPassword = hashHmacPasswordEncoder.encode(password);

        Assertions.assertFalse(PasswordHash.matches("Test456", encodedPassword));
    }

}
