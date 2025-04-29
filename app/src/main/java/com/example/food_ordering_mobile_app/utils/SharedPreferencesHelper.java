package com.example.food_ordering_mobile_app.utils;
import android.content.Context;
import android.content.SharedPreferences;

//import com.example.food_ordering_mobile_app.models.foodType.FoodType;
//import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.user.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
public class SharedPreferencesHelper {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_INFO = "user_info";
    private static final String USER_ROLE = "user_role";
    private static final String USER_STORE_ID = "user_store_id";
    private static final String USER_STORE_OWNER = "user_store_owner";


    private static SharedPreferencesHelper instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson = new Gson();


    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPreferencesHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesHelper(context.getApplicationContext());
        }
        return instance;
    }

    public void saveUserData(String accessToken, String userId, ArrayList<String> roles, String storeId, String owner) {
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(USER_ROLE, gson.toJson(roles));
        editor.putString(USER_STORE_ID, storeId);
        editor.putString(USER_STORE_OWNER, owner);
        editor.apply();
    }

    public String getRoles() {
        return sharedPreferences.getString(USER_ROLE, null);
    }
    public String getStoreId() {
        return sharedPreferences.getString(USER_STORE_ID, null);
    }
    public String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }
    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }
    public String getOwnerId(){return sharedPreferences.getString(USER_STORE_OWNER, null);}

    public void saveCurrentUser(User user) {
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER_INFO, userJson);
        editor.apply();
    }

    public User getCurrentUser() {
        String userJson = sharedPreferences.getString(KEY_USER_INFO, null);
        return userJson != null ? gson.fromJson(userJson, User.class) : null;
    }

    public void clearUserData() {
        editor.clear();
        editor.apply();
    }
}