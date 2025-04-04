package com.example.food_ordering_mobile_app.models.location;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {
    private String type;
    private ArrayList<Float> coordinates;
    private String address;
    private String detailAddress;

    public Location() {
        // Default constructor
        type = "Point";
        coordinates = new ArrayList<>();
        address = "";
        detailAddress = "";
    }

}
