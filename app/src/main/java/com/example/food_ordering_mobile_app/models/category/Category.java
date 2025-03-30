package com.example.food_ordering_mobile_app.models.category;

public class Category {

    private String id;
    private String name;
    private int imageUrl;


    private int displayOrder;

    public Category(String id, String name, int imageUrl, int displayOrder) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }
}