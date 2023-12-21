package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Utils {
    private Utils() {
    }

    public static String hashPassword(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];

        random.nextBytes(salt);

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        md.update(salt);

        var stringBuilder = new StringBuilder();

        var hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        for (byte b : hashedPassword) {
            stringBuilder.append(b);
        }

        return stringBuilder.toString();
    }

    public static <T> void log(T value) {
        if (value == null) throw new RuntimeException("Null value provided");

        System.out.println(value);
    }
}
