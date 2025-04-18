package com.example.food_ordering_mobile_app.models.chat;

import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.user.User;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Chat implements Serializable {
    @SerializedName("_id")
    private String id;
    private List<User> users;
    private Message latestMessage;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Store store;

    public Chat(String id, List<User> users, Message latestMessage, Timestamp updatedAt) {
        this.id = id;
        this.users = users;
        this.latestMessage = latestMessage;
        this.updatedAt = updatedAt;
    }
}