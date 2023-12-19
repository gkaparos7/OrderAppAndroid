package gr.aueb.cf4.orderappandroid.utils;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtUtils {

    // Use the secret key obtained from the property
    private static final SecretKey SECRET_KEY = generateSecretKey("yourSecretKey123456");

    private static SecretKey generateSecretKey(String keyString) {
        try {
            // Use SHA-512 to generate a hash of the key string
            MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
            byte[] keyBytes = sha512.digest(keyString.getBytes(StandardCharsets.UTF_8));

            // Use the first 512 bits (64 bytes) as the key
            byte[] truncatedKey = new byte[64];
            System.arraycopy(keyBytes, 0, truncatedKey, 0, 64);

            return Keys.hmacShaKeyFor(truncatedKey);
        } catch (NoSuchAlgorithmException e) {
            Log.e("Key Generation", "Error generating secret key: " + e.getMessage());
            throw new RuntimeException("Error generating secret key", e);
        }
    }

    public static String extractUserNameFromToken(String authToken) {
        Log.d("Token", authToken); // Log the token before parsing
        if (authToken == null || !authToken.startsWith("Bearer ")) {
            Log.d("Token", "Invalid Token Format");
            return "Unknown User";
        }

        // Remove the "Bearer " prefix to get the token
        String[] parts = authToken.split(" ");
        if (parts.length == 2) {
            String token = parts[1];
            Log.d("Token", "Token after removing 'Bearer ': " + token);

            try {
                Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
                return claims.getSubject();
            } catch (Exception e) {
                // Handle exceptions (e.g., token expired, invalid token)
                Log.e("Token", "Exception during token parsing: " + e.getMessage());
                return "Unknown User";
            }
        } else {
            Log.d("Token", "Invalid Token Format");
            return "Unknown User";
        }
    }
}
