package gemeente.authorizationserver;

import java.util.Base64;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Base64CredentialCreator {

    private static final String USERNAME = "first-app";
    private static final String PASSWORD = "12345";

    @Test
    public void testEncoding() {
        System.out.println("Basic " + Base64.getEncoder().encodeToString((USERNAME + ":" + PASSWORD).getBytes()));
    }

    @Test
    public void testEncodingBCrypt() {
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String encoded = encoder.encode(USERNAME + ":" + PASSWORD);
        System.out.println("Basic " + Base64.getEncoder().encodeToString(encoded.getBytes()));
    }
}
