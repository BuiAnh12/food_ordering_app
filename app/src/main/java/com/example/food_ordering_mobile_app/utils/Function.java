package com.example.food_ordering_mobile_app.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

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

    public static String dateConverter(String isoDateString, String outputFormatString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Parse as UTC

            SimpleDateFormat outputFormat = new SimpleDateFormat(outputFormatString);

            Date date = inputFormat.parse(isoDateString);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown Date";
        }
    }

    public static String priceConverter(double price) {
        String result;
        try {
            result = String.format("%,.0f", price);
        } catch (Exception e) {
            e.printStackTrace();
            result = String.valueOf(price);
        }
        return result;
    }


    private static final double EARTH_RADIUS = 6371; // Bán kính trái đất tính bằng km
    private static final double AVERAGE_SPEED_KMH = 30.0; // Average speed in km/h
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c; // Distance in km
    }

    public static String calculateEstimatedArrivalTime(double lat1, double lon1, double lat2, double lon2, String orderStartTime) {
        // Calculate the distance
        double distance = calculateDistance(lat1, lon1, lat2, lon2);

        // Calculate the estimated travel time (in hours)
        double travelTimeHours = distance / AVERAGE_SPEED_KMH;
        long travelTimeMinutes = (long) (travelTimeHours * 60);
        long hours = travelTimeMinutes / 60;
        long minutes = travelTimeMinutes % 60;

        // Parse the start order time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime orderTime = LocalTime.parse(orderStartTime, formatter);
        LocalTime estimatedArrivalTime = orderTime.plusMinutes(travelTimeMinutes);

        // Format the output
        if (hours == 0) {
            // Less than 1 hour, show mm:ss
            return String.format("%02d:%02d", minutes, estimatedArrivalTime.getSecond());
        } else {
            // More than 1 hour, show hh:mm:ss
            return estimatedArrivalTime.format(formatter);
        }
    }

    private String fixNonStandardJson(String rawJson) {
        return rawJson.replaceAll("new ObjectId\\(\"(.*?)\"\\)", "\"$1\"");
    }

}
