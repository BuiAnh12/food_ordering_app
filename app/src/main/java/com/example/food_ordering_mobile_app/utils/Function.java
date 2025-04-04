package com.example.food_ordering_mobile_app.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Function {
    public static String generateOrderNumber(String orderId) {
        try {
            // Create MD5 hash of the orderId
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(orderId.getBytes());
            byte[] digest = md.digest();

            // Convert first 4 bytes to an integer (equivalent to substring(0, 8) in hex)
            int number = ((digest[0] & 0xFF) << 24) | ((digest[1] & 0xFF) << 16)
                    | ((digest[2] & 0xFF) << 8) | (digest[3] & 0xFF);

            // Ensure non-negative and within 6 digits
            number = Math.abs(number % 1000000);

            // Return as a 6-digit string
            return String.format("%06d", number);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }
}
