package com.example.food_ordering_mobile_app.models.notification;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Notification {
    @SerializedName("_id")
    private String id;
    private String userId;
    private String title;
    private String message;
    private String type;
    private String status;
    private String orderId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Notification(String id, String uId, String title, String message, String type, String status, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.userId = uId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Notification(String id, String userId, String title, String message, String type, String status, String orderId, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.status = status;
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    public String getRelativeTime() {
        long now = System.currentTimeMillis();
        long diff = now - createdAt.getTime();

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (seconds < 60) return "just now";
        else if (minutes < 60) return minutes + " min ago";
        else if (hours < 24) return hours + " hr ago";
        else return days + " day" + (days > 1 ? "s" : "") + " ago";
    }
}
