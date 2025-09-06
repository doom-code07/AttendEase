package utils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

public class TokenUtil {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static LocalDateTime expiryAfterHours(int hours) {
        return LocalDateTime.now().plusHours(hours);
    }
}
