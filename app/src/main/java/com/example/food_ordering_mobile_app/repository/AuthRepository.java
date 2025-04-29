package com.example.food_ordering_mobile_app.repository;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.network.services.AuthService;
import com.example.food_ordering_mobile_app.network.RetrofitClient;
import com.example.food_ordering_mobile_app.utils.PersistentCookieStore;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;

import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class AuthRepository {
    private AuthService authService;
    private SharedPreferencesHelper sharedPreferencesHelper;
    public AuthRepository(Context context) {
        authService = RetrofitClient.getClient(context).create(AuthService.class);
        sharedPreferencesHelper = new SharedPreferencesHelper(context);

        CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();

        if (!(cookieManager.getCookieStore() instanceof PersistentCookieStore)) {
            Log.e("AuthRepository", "CookieStore chưa đúng, thiết lập lại...");
            cookieManager = new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(cookieManager);
        } else {
            Log.d("AuthRepository", "PersistentCookieStore đã được thiết lập!");
        }
    }

    private void saveCookies(Response<User> response) {
        CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
        if (!(cookieManager.getCookieStore() instanceof PersistentCookieStore)) {
            return;
        }

        List<String> cookies = response.headers().values("Set-Cookie");
        if (cookies == null || cookies.isEmpty()) {
            return;
        }

        PersistentCookieStore cookieStore = (PersistentCookieStore) cookieManager.getCookieStore();

        for (String cookie : cookies) {
            try {
                List<HttpCookie> parsedCookies = HttpCookie.parse(cookie);
                for (HttpCookie httpCookie : parsedCookies) {
                    URI uri = new URI("http://192.168.0.57:5000/"); // Có thể cần sửa theo domain thực tế của cookie
                    cookieStore.add(uri, httpCookie);
                    Log.d("SaveCookies", "Saved Cookie: " + httpCookie.toString());
                }
            } catch (Exception e) {
                Log.e("SaveCookies", "Lỗi khi parse cookie!", e);
            }
        }
    }

    private void clearCookies(Context context) {
        CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
        if (cookieManager != null) {
            cookieManager.getCookieStore().removeAll();
            Log.d("ClearCookies", "Tất cả cookie đã bị xóa!");
        }
    }

    public LiveData<Resource<User>> login(String email, String password) {
        MutableLiveData<Resource<User>> loginLiveData  = new MutableLiveData<>();
        loginLiveData.setValue(Resource.loading(null)); // Trạng thái Loading

        User request = new User(email, password);
        authService.login(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String accessToken = response.body().getAccessToken();
                    String userId = response.body().getId();
                    String storeId = response.body().getStoreId();
                    String owner = response.body().getOwnerId();
                    ArrayList<String> role = response.body().getRole();

                    // Lưu vào SharedPreferences
                    sharedPreferencesHelper.saveUserData(accessToken, userId, role, storeId, owner);

                    saveCookies(response);

                    loginLiveData.setValue(Resource.success("Đăng nhập thành công!", response.body()));
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");

                        loginLiveData.setValue(Resource.error(message, null));
                    } catch (Exception e) {
                        loginLiveData.setValue(Resource.error("Lỗi không xác định!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("AuthRepository", "Login request failed: " + t.getMessage(), t);
                loginLiveData.setValue(Resource.error("Lỗi kết nối", null));
            }
        });

        return loginLiveData ;
    }
    public MutableLiveData<Resource<String>> logout(Context context) {
        MutableLiveData<Resource<String>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null)); // Trạng thái Loading

        authService.logout().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Logout", "Logout successful, clearing data...");

                    sharedPreferencesHelper.clearUserData();

                    // Xóa tất cả cookie
                    clearCookies(context);
                    CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
                    cookieManager.getCookieStore().removeAll();

                    result.setValue(Resource.success("Logout thành công!", null));
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Lỗi không xác định!";
                        JSONObject jsonObject = new JSONObject(errorMessage);
                        String message = jsonObject.getString("message");
                        result.setValue(Resource.error(message, null));
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi khi đọc phản hồi từ server!", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối", null));
            }
        });

        return result;
    }

    public MutableLiveData<Resource<Store>> getOwnStore() {
        MutableLiveData<Resource<Store>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null)); // Trạng thái Loading
        authService.ownStore().enqueue(new Callback<Store>() {
            @Override
            public void onResponse(Call<Store> call, Response<Store> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success("Lấy thông tin cửa hàng thành công!", response.body()));
                }
                else {
                    result.setValue(Resource.error("Lỗi không xác định!", null));
                }
            }

            @Override
            public void onFailure(Call<Store> call, Throwable throwable) {
                result.setValue(Resource.error("Lỗi không xác định!", null));
            }
        });
        return result;
    }
}
