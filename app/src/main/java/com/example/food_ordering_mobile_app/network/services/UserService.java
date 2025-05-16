package com.example.food_ordering_mobile_app.network.services;
import com.example.food_ordering_mobile_app.models.user.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @GET("/api/v1/user/{id}")
    Call<User> getUserById(@Path("id") String id);

    @PUT("/api/v1/user")
    Call<User> updateUser(@Body User user);
}
