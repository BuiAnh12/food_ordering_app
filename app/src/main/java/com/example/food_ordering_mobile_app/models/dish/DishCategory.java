package com.example.food_ordering_mobile_app.models.dish;

import java.util.List;

public class DishCategory {
    private String categoryName;
    private List<Dish> items;

    public DishCategory(String categoryName, List<Dish> items) {
        this.categoryName = categoryName;
        this.items = items;
    }

    public String getCategoryName() { return categoryName; }
    public List<Dish> getItems() { return items; }
}
