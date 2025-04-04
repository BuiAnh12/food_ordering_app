package com.example.food_ordering_mobile_app.models.siteSetting;

import android.app.Application;

import com.example.food_ordering_mobile_app.authorization.SecurityManager;
import com.example.food_ordering_mobile_app.models.user.User;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GlobalVariable extends Application {
    private String appName = "com.example.food_ordering_mobile_app";
    private String accessToken;

    private String contentType = "application/x-www-form-urlencoded";
    private Map<String, String> headers;


    private User AuthUser;
    private SiteSettings appInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize SecurityManager once when the app starts
        SecurityManager.init(this);
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = "JWT " + accessToken;
    }
    public Map<String, String> getHeaders() {

        this.headers = new HashMap<>();
        this.headers.put("Content-Type", contentType );
        this.headers.put("Authorization", accessToken);

        return headers;
    }

}
