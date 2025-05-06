package com.example.food_ordering_mobile_app.models.category;

import com.example.food_ordering_mobile_app.models.image.Image;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FoodType {
    @SerializedName("_id")
    String id;
    String name;
    Image image;
}
