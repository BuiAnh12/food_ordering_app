package com.example.food_ordering_mobile_app.authorization;

import android.content.Context;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecurityManager {
    private static final List<String> ROLE_PRIORITY = Arrays.asList("owner", "manager", "staff", "user");
    private static SharedPreferencesHelper sharedPreferencesHelper;

    // Initialize the SecurityManager with application context
    public static void init(Context context) {
        if (sharedPreferencesHelper == null) {
            sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context);
        }
    }

    public static String getHighestRole() {
        if (sharedPreferencesHelper == null) {
            throw new IllegalStateException("SecurityManager not initialized. Call SecurityManager.init(context) first.");
        }

        String rolesJson = sharedPreferencesHelper.getRoles();

        if (rolesJson == null || rolesJson.isEmpty()) {
            return null; // No roles found
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> roles = gson.fromJson(rolesJson, listType);

        if (roles == null || roles.isEmpty()) {
            return null; // No valid roles
        }

        // Find the highest-priority role (ignoring "user")
        for (String role : ROLE_PRIORITY) {
            if (roles.contains(role) && !role.equals("user")) {
                return role;
            }
        }

        return "user"; // Default to "user" if no higher role found
    }

    public static boolean isLogin() {
        if (sharedPreferencesHelper == null) {
            throw new IllegalStateException("SecurityManager not initialized. Call SecurityManager.init(context) first.");
        }

        String accessToken = sharedPreferencesHelper.getAccessToken();
        String userId = sharedPreferencesHelper.getUserId();
        String rolesJson = sharedPreferencesHelper.getRoles();
        String storeId = sharedPreferencesHelper.getStoreId();

        // User is logged in if all required fields exist
        if (accessToken == null || accessToken.isEmpty() ||
                userId == null || userId.isEmpty() ||
                storeId == null || storeId.isEmpty()) {
            return false;
        }

        // Ensure at least one valid role exists
        if (rolesJson == null || rolesJson.isEmpty()) {
            return false;
        }

        return true; // User is logged in
    }
    public static boolean isOwner() {
        return "owner".equals(getHighestRole());
    }
    public static boolean isManager() {
        return "manager".equals(getHighestRole());
    }
    public static boolean isStaff() {
        return "staff".equals(getHighestRole());
    }
    public static boolean isUser() {
        return "user".equals(getHighestRole());
    }
}
