package com.example.food_ordering_mobile_app.models.dish;

import com.example.food_ordering_mobile_app.models.category.Category;
import com.example.food_ordering_mobile_app.models.image.Image;
import com.example.food_ordering_mobile_app.models.topping.ToppingGroup;
import com.example.food_ordering_mobile_app.network.SingleOrListOrIdDeserializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dish {
    @SerializedName("_id")
    private String id;
    private String name;
    private double price;
    private Category category;
    @SerializedName("store")
    private String storeId;
    private Image image;

    private List<ToppingGroup> toppingGroups;
    private String description;

    public Dish() {
        // Default constructor
        id = "";
        name = "";
        price = 0.0;
        category = new Category();
        storeId = "";
        image = new Image();
        toppingGroups = null;
        description = "";
    }


}
