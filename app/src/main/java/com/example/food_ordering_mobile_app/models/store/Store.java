package com.example.food_ordering_mobile_app.models.store;

import com.example.food_ordering_mobile_app.models.category.Category;
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

@Getter
@Setter
public class Store {
    @SerializedName("_id")
    private String id;
    private String name;
    private String owner;
    private String description;
    private Location address;

    private List<String> storeCategory;
    private Image avatar;
    private Image cover;
    private PaperWork paperWork;

    private List<String> staff;
    private boolean isApproved;

    private Shipper shipper;

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

