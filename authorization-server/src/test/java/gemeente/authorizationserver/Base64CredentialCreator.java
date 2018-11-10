package gemeente.authorizationserver;

import java.util.Base64;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertTrue;

public class Base64CredentialCreator {

    private static final String USERNAME = "first-app";
    private static final String PASSWORD = "12345";

    @Test
    public void testEncoding() {
        System.out.println("Basic " + Base64.getEncoder().encodeToString((USERNAME + ":" + PASSWORD).getBytes()));
    }

    @Test
    public void testEncodingBCryptToBase64() {
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String encoded = encoder.encode(USERNAME + ":" + PASSWORD);
        System.out.println("Basic " + Base64.getEncoder().encodeToString(encoded.getBytes()));
    }

    @Test
    public void testEncodingBCrypt() {
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);
        System.out.println("Encoded password: " + encoder.encode("test123"));
    }

    @Test
    public void testMatches() {
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches("test123", "$2a$11$7h0hncHpczNUtibioH9GxuyLHLzUnDqZNwWWG5S5Op11iSkXwtCFi"));
    }
}
