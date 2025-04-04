package com.example.food_ordering_mobile_app.models.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class OrderDateGroup {
    public static List<OrderDateGroup> groupOrdersByDate(List<Order> orders) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure parsing as UTC
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Group orders by their date (extracted from createdAt)
        Map<String, List<Order>> groupedOrders = orders.stream()
                .collect(Collectors.groupingBy(order -> {
                    try {
                        Date date = inputFormat.parse(order.getCreatedAt());
                        return outputFormat.format(date); // Convert to "yyyy-MM-dd"
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "Unknown Date";
                    }
                }));

        // Convert Map to List of OrderDateGroup
        List<OrderDateGroup> orderGroups = groupedOrders.entrySet().stream()
                .map(entry -> new OrderDateGroup(entry.getKey(), entry.getValue()))
                .sorted((a, b) -> b.getDate().compareTo(a.getDate())) // Sort by date (newest first)
                .collect(Collectors.toList());

        return orderGroups;
    }

    private String date;
    private List<Order> orders;

    public OrderDateGroup(String date, List<Order> orders) {
        this.date = date;
        this.orders = orders;
    }

    public String getDate() { return date; }
    public List<Order> getOrders() { return orders; }
}
