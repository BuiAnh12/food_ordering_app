package com.example.food_ordering_mobile_app.models.topping;

import java.util.List;

public class ToppingGroup {
    String id;
    List<Topping> toppingList;
    String groupName;

    String storeId;


    public ToppingGroup(String id, List<Topping> toppingList, String groupName, String storeId) {
        this.id = id;
        this.toppingList = toppingList;
        this.groupName = groupName;
        this.storeId = storeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Topping> getToppingList() {
        return toppingList;
    }

    public void setToppingList(List<Topping> toppingList) {
        this.toppingList = toppingList;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
