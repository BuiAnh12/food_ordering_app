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

    @Override
    public String toString() {
        return "Notification{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
