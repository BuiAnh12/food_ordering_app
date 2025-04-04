package com.example.food_ordering_mobile_app.models.topping;

import com.example.food_ordering_mobile_app.network.SingleOrListOrIdDeserializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Topping {
    @SerializedName("_id")
    private String id;
    private String name;
    private double price;

    @JsonAdapter(SingleOrListOrIdDeserializer.class)
    private ToppingGroup toppingGroup;

    public Topping() {
    }

    public Topping(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
