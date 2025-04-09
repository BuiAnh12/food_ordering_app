package com.example.food_ordering_mobile_app.models.order;

import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.topping.Topping;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderItem implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("dish")
    private Dish dish;
    private int quantity;
    private ArrayList<Topping> toppings;

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
