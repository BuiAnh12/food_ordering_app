package com.example.food_ordering_mobile_app.network.services;

import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.user.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
public interface AuthService {
    @POST("auth/register")
    Call<User> register(@Body User user);
    @POST("auth/login?getRole=true&getStore=true")
    Call<User> login(@Body User user);
    @GET("auth/logout")
    Call<String> logout();
    @GET("auth/refresh")
    Call<User> refreshToken();
    @PUT("auth/change-password")
    Call<String> changePassword(@Body Map<String, String> data);
    @POST("auth/store")
    Call<Store> ownStore();

    @POST("auth/refresh/mobile")
    Call<User> refreshTokenMobile(@Body String refreshToken);
}
