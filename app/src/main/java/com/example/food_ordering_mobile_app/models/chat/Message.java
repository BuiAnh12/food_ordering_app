package com.example.food_ordering_mobile_app.models.chat;

import java.io.Serializable;
import com.example.food_ordering_mobile_app.models.image.Image;
import com.example.food_ordering_mobile_app.models.user.User;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Message implements Serializable {
    @SerializedName("_id")
    private String id;
    private User sender;
    private String content;
    private Image image;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
