package com.example.food_ordering_mobile_app.models.dish;

import java.util.*;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishCategory {
    private String categoryName;
    private List<Dish> items;

    public DishCategory(String categoryName, List<Dish> items) {
        this.categoryName = categoryName;
        this.items = items;
    }

    public static List<DishCategory> groupDishesByCategory(List<Dish> dishes) {
        if (dishes == null || dishes.isEmpty()) {
            return new ArrayList<>();
        }

        // Group dishes by category name
        Map<String, List<Dish>> groupedDishes = dishes.stream()
                .collect(Collectors.groupingBy(dish -> dish.getCategory().getName()));  // Use category name, not object

        return groupedDishes.entrySet().stream()
                .map(entry -> new DishCategory(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}


