package com.example.food_ordering_mobile_app.network.services;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.topping.ToppingGroup;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface DishService {

    @GET("dish/store/{store_id}")
    Call <ApiResponse<List<Dish>>> getAllDishes(@Path("store_id") String storeId,
                                                @Query("name") String name,
                                                @Query("limit") Integer limit,
                                                @Query("page") Integer page);

    @GET("dish/{dish_id}")
    Call<ApiResponse<Dish>> getDish(@Path("dish_id") String dishId);

    @GET("dish/{dish_id}/rating/avg")
    Call<ApiResponse<Float>> getAvgRating(@Path("dish_id") String dishId);

    @GET("topping/dish/{dish_id}")
    Call<ApiResponse<List<ToppingGroup>>> getToppingsFromDish(@Path("dish_id") String dishId);

    @PUT("dish/{dish_id}")
    Call<ApiResponse> updateDish(@Path("dish_id") String dishId, @Body Dish dish);

    @POST("dish/store/{store_id}")
    Call<ApiResponse> createDish(@Path("store_id") String storeId, @Body Dish dish);

    @DELETE("dish/{dish_id}")
    Call<ApiResponse> deleteDish(@Path("dish_id") String dishId);

    @POST("dish/{dish_id}/saleStatus")
    Call<ApiResponse> changeStockStatus(@Path("dish_id") String dishId);
}