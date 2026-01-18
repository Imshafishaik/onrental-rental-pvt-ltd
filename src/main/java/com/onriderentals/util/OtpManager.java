package com.onriderentals.util;

import java.security.SecureRandom;
import java.util.*;

public class OtpManager {
    private static final Map<String, OtpData> otpStore = new HashMap<>();
    private static final SecureRandom random = new SecureRandom();

    private static class OtpData {
        String otp;
        long expiryTime; // System time when OTP expires
        String email;

        OtpData(String otp, long expiryTimeMillis, String email) {
            this.otp = otp;
            this.expiryTime = expiryTimeMillis;
            this.email = email;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    /**
     * Generate and store OTP for user
     * 
     * @param email           User email
     * @param validityMinutes How long OTP is valid (in minutes)
     * @return Generated OTP code
     */
    public static String generateOtp(String email, int validityMinutes) {
        // Generate 6-digit OTP
        int otpCode = 100000 + random.nextInt(900000);
        String otp = String.valueOf(otpCode);

        // Calculate expiry time
        long expiryTime = System.currentTimeMillis() + (validityMinutes * 60 * 1000);

        // Store OTP
        otpStore.put(email, new OtpData(otp, expiryTime, email));

        System.out.println("OTP generated for " + email + ": " + otp + " (Valid for " + validityMinutes + " minutes)");
        return otp;
    }

    /**
     * Generate OTP with default validity from configuration
     */
    public static String generateOtp(String email) {
        int validityMinutes = ConfigManager.getInt("OTP_VALIDITY_MINUTES", 5);
        return generateOtp(email, validityMinutes);
    }

    /**
     * Verify OTP for user
     * 
     * @param email User email
     * @param otp   OTP code to verify
     * @return true if OTP is valid and not expired
     */
    public static boolean verifyOtp(String email, String otp) {
        OtpData otpData = otpStore.get(email);

        if (otpData == null) {
            System.err.println("No OTP found for email: " + email);
            return false;
        }

        if (otpData.isExpired()) {
            System.err.println("OTP expired for email: " + email);
            otpStore.remove(email); // Clean up expired OTP
            return false;
        }

        if (!otpData.otp.equals(otp)) {
            System.err.println("Invalid OTP for email: " + email);
            return false;
        }

        // OTP verified successfully, remove it from store
        otpStore.remove(email);
        System.out.println("OTP verified successfully for email: " + email);
        return true;
    }

    /**
     * Get remaining validity time for OTP in seconds
     */
    public static int getOtpRemainingSeconds(String email) {
        OtpData otpData = otpStore.get(email);
        if (otpData == null || otpData.isExpired()) {
            return 0;
        }
        return (int) ((otpData.expiryTime - System.currentTimeMillis()) / 1000);
    }

    /**
     * Clear OTP for email (useful after password reset)
     */
    public static void clearOtp(String email) {
        otpStore.remove(email);
    }

    /**
     * Clean up expired OTPs (run periodically)
     */
    public static void cleanupExpiredOtps() {
        otpStore.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
}
