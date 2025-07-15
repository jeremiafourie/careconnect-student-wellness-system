package com.bc.shared.utils;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for generating and managing secure tokens.
 * 
 * This class provides methods for generating cryptographically secure
 * random tokens used for remember me functionality and other security
 * purposes.
 * 
 * @author CareConnect Development Team
 * @version 1.0
 * @since 1.0
 */
public class TokenUtils {
    
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int TOKEN_LENGTH = 32; // 32 bytes = 256 bits
    
    /**
     * Generates a cryptographically secure random token.
     * 
     * The token is generated using SecureRandom and encoded in Base64
     * for safe transmission and storage. The token has 256 bits of entropy.
     * 
     * @return A secure random token string
     */
    public static String generateSecureToken() {
        byte[] tokenBytes = new byte[TOKEN_LENGTH];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
    
    /**
     * Validates that a token is not null or empty.
     * 
     * @param token The token to validate
     * @return true if the token is valid (not null and not empty), false otherwise
     */
    public static boolean isValidToken(String token) {
        return token != null && !token.trim().isEmpty();
    }
}