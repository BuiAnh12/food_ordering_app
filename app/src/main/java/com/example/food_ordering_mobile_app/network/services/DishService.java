package com.example.food_ordering_mobile_app.network.services;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.rating.Rating;
import com.example.food_ordering_mobile_app.models.topping.Topping;
import com.example.food_ordering_mobile_app.models.topping.ToppingGroup;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface DishService {

    @GET("/api/v1/store/{store_id}/dish")
    Call <ApiResponse<List<Dish>>> getAllDishes(@Path("store_id") String storeId,
                                                @Query("name") String name,
                                                @Query("limit") Integer limit,
                                                @Query("page") Integer page);

    @GET("/api/v1/store/dish/{dish_id}")
    Call<ApiResponse<Dish>> getDish(@Path("dish_id") String dishId);

    @GET("/api/v1/store/dish/{dish_id}/rating/avg")
    Call<ApiResponse<Float>> getAvgRating(@Path("dish_id") String dishId);

    @GET("/api/v1/store/dish/{dish_id}/rating")
    Call<ApiResponse<List<Rating>>> getAllRatings(@Path("dish_id") String dishId);

    @GET("/api/v1/store/{store_id}/rating/avg")
    Call<ApiResponse<Float>> getAvgStoreRating(@Path("store_id") String storeId);

    @GET("/api/v1/store/{storeId}/rating")
    Call<ApiResponse<List<Rating>>> getAllStoreRatings(@Path("storeId") String storeId);

    @GET("/api/v1/store/dish/{dish_id}/topping")
    Call<ApiResponse<List<ToppingGroup>>> getToppingsFromDish(@Path("dish_id") String dishId);

    @POST("/api/v1/store/dish/{dish_id}/topping")
    Call<Void> addToppingToDish(@Path("dish_id") String dishId, @Body Topping topping);

    @PUT("/api/v1/store/dish/{dish_id}")
    Call<ApiResponse<Dish>> updateDish(@Path("dish_id") String dishId, @Body Dish dish);

    @POST("/api/v1/store/{store_id}/dish/add")
    Call<ApiResponse<Dish>> createDish(@Path("store_id") String storeId, @Body Dish dish);
}