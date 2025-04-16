package com.example.food_ordering_mobile_app.models.category;

import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.network.SingleOrListOrIdDeserializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Category {

    @SerializedName("_id")
    private String id;
    private String name;
    private String store;

    @JsonAdapter(SingleOrListOrIdDeserializer.class)
    private List<Dish> dishes;

    public Category() {
        // Default constructor
        id = "";
        name = "";
        store = "";
        dishes = null;
    }

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name; // this will be shown in the Spinner
    }

}