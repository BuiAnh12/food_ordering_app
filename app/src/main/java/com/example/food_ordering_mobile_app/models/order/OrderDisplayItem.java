package com.example.food_ordering_mobile_app.models.order;

import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.topping.Topping;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDisplayItem {
    private boolean isTopping;
    private Dish dish;           // Used if isTopping is false
    private Topping topping;     // Used if isTopping is true
    private int quantity;

    public OrderDisplayItem(Dish dish, int quantity) {
        this.isTopping = false;
        this.dish = dish;
        this.quantity = quantity;
    }

    public OrderDisplayItem(Topping topping) {
        this.isTopping = true;
        this.topping = topping;

    }

}
