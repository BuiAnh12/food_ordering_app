package com.example.food_ordering_mobile_app.models.user;
import com.example.food_ordering_mobile_app.models.image.Image;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
    @SerializedName("_id")
    private String id;
    private String name;
    private String email;
    private String phonenumber;
    private String password;
    private String gender;
    @SerializedName("role")
    private ArrayList<String> role;
    private Image avatar;
    @SerializedName("token")
    private String accessToken;
    @SerializedName("refreshToken")
    private String refreshToken;
    private Boolean isGoogleLogin;
    @SerializedName("storeId")
    private String storeId;
    private String ownerId;

    public User() {
        // Default constructor
        this.id = "";
        this.name = "";
        this.email = "";
        this.phonenumber = "";
        this.password = "";
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public User(String name, String email, String phonenumber, String gender, String password) {
        this.name = name;
        this.email = email;
        this.phonenumber = phonenumber;
        this.gender = gender;
        this.password = password;
    }



    public String getTopRole() {
        if (role != null && !role.isEmpty()) {
            return role.get(0);
        }
        return "";
    }
}
