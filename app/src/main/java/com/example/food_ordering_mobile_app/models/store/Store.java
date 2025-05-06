package com.example.food_ordering_mobile_app.models.store;

import com.example.food_ordering_mobile_app.models.category.Category;
import com.example.food_ordering_mobile_app.models.category.FoodType;
import com.example.food_ordering_mobile_app.models.image.Image;
import com.example.food_ordering_mobile_app.models.location.Location;
import com.example.food_ordering_mobile_app.models.shipper.Shipper;
import com.example.food_ordering_mobile_app.models.user.User;
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
public class Store {
    @SerializedName("_id")
    private String id;
    private String name;
    private String owner;
    private String description;
    private Location address;


    private List<FoodType> storeCategory;
    private Image avatar;
    private Image cover;
    private PaperWork paperWork;

    private List<String> staff;
    private boolean isApproved;

    private Shipper shipper;

    private String avgRating;
    private String amountRating;
    private String status;

    public Store() {
        // Default constructor
        id = "";
        name = "";
        owner = "";
        description = "";
        address = new Location();
        storeCategory = null;
        avatar = new Image();
        cover = new Image();
        paperWork = new PaperWork();
        staff = null;
        isApproved = false;
        shipper = new Shipper();
    }

    @Getter
    @Setter
    public static class PaperWork {
        private Image ICFront;
        private Image ICBack;
        private Image businessLicense;
        private List<Image> storePicture;
        PaperWork(){
            ICFront = new Image();
            ICBack = new Image();
            businessLicense = new Image();
            storePicture = null;
        }

    }

}

