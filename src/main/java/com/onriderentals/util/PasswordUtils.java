package com.onriderentals.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    /**
     * Hash a password using BCrypt.
     * @param plainPassword the plain text password
     * @return the hashed password
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /**
     * Verify a password against a hashed password.
     * @param plainPassword the plain text password
     * @param hashedPassword the previously hashed password
     * @return true if the password matches, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            // Support legacy plain text passwords during transition if needed
            // But for a new/clean implementation, we should probably just return false
            // or return plainPassword.equals(hashedPassword) if we want to be backwards compatible.
            return plainPassword.equals(hashedPassword);
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}