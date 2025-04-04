package com.example.food_ordering_mobile_app.models.rating;

import com.google.gson.annotations.SerializedName;

public class Rating {
    @SerializedName("_id")
    private String id;
    private String user;
    private String store;
    private int rating;
    private String comment;
    private String date;

}
