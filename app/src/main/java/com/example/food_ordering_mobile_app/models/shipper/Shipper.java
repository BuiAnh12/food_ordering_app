package com.example.food_ordering_mobile_app.models.shipper;

import com.example.food_ordering_mobile_app.models.image.Image;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Shipper {
    @SerializedName("_id")
    private String id;
    private String name;
    private String email;
    private String phonenumber;
    private String gender;
    @SerializedName("role")
    private ArrayList<String> role;
    private Image avatar;
    @SerializedName("token")
    private String accessToken;
    private Boolean isGoogleLogin;
    @SerializedName("storeId")
    private String storeId;

    public Shipper(){
        id = "";
        name = "";
        email = "";
        phonenumber = "";
        gender = "Default";
        role = new ArrayList<>();
        avatar = new Image();
        accessToken = "";
        isGoogleLogin = null;
        storeId = "";
    }
}
