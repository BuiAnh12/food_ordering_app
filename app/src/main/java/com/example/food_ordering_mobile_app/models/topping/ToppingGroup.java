package com.example.food_ordering_mobile_app.models.topping;

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
public class ToppingGroup {
    @SerializedName("_id")
    private String id;
    private String name;
    @SerializedName("store")
    private String storeId;

    private List<Topping> toppings;

    public ToppingGroup() {

    }

    public ToppingGroup(String id, List<Topping> toppings, String groupName, String storeId) {
        this.id = id;
        this.toppings = toppings;
        this.name = groupName;
        this.storeId = storeId;
    }
}
