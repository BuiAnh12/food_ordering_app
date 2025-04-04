package com.example.food_ordering_mobile_app.models.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class OrderDateStatusGroup {
    private String date;
    private String status;
    private List<Order> orders;

    public static List<OrderDateStatusGroup> groupOrdersByDateAndStatus(List<Order> orders) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Handle UTC time
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        Map<String, Map<String, List<Order>>> groupedMap = new HashMap<>();

        for (Order order : orders) {
            try {
                Date date = inputFormat.parse(order.getCreatedAt());
                String formattedDate = outputFormat.format(date); // Extract only the date

                String status = order.getStatus();

                groupedMap
                        .computeIfAbsent(formattedDate, k -> new HashMap<>()) // Ensure date exists
                        .computeIfAbsent(status, k -> new ArrayList<>()) // Ensure status exists
                        .add(order);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Convert to List<OrderDateStatusGroup>
        List<OrderDateStatusGroup> groupedOrders = groupedMap.entrySet().stream()
                .flatMap(dateEntry -> dateEntry.getValue().entrySet().stream()
                        .map(statusEntry -> new OrderDateStatusGroup(dateEntry.getKey(), statusEntry.getKey(), statusEntry.getValue())))
                .sorted((a, b) -> b.getDate().compareTo(a.getDate())) // Sort newest date first
                .collect(Collectors.toList());

        return groupedOrders;
    }

    public OrderDateStatusGroup(String date, String status, List<Order> orders) {
        this.date = date;
        this.orders = orders;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
    public String getDate() { return date; }
    public List<Order> getOrders() { return orders; }
}
