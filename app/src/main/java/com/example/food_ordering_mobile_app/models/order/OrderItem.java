package com.example.food_ordering_mobile_app.models.order;

import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderItem {
    @SerializedName("_id")
    private String id;
    @SerializedName("dish")
    private Dish dish;
    private int quantity;

    public OrderItem() {
        // Default constructor
        id = "";
        dish = new Dish();
        quantity = 0;
    }


    public OrderItem(String id, Dish dish, int quantity) {
        this.id = id;
        this.dish = dish;
        this.quantity = quantity;
    }
}
