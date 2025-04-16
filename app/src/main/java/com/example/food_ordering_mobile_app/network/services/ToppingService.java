package com.example.food_ordering_mobile_app.network.services;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.topping.Topping;
import com.example.food_ordering_mobile_app.models.topping.ToppingGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ToppingService {

    // Get all toppings for a specific store with pagination
    @GET("/api/v1/store/{store_id}/topping")
    Call<ApiResponse<List<ToppingGroup>>> getAllToppings(@Path("store_id") String storeId,
                                                         @Query("limit") Integer limit,
                                                         @Query("page") Integer page);

    // Get a specific topping by its ID
    @GET("/api/v1/store/topping-group/{group_id}")
    Call<ApiResponse<ToppingGroup>> getTopping(@Path("group_id") String groupId);

    // Add a topping to a specific topping group
    @POST("/api/v1/store/topping-group/{group_id}/topping")
    Call<ApiResponse<Void>> addToppingToGroup(@Path("group_id") String groupId,
                                                 @Body Topping topping);

    // Update a topping in a specific topping group
    @PUT("/api/v1/store/topping-group/{group_id}/topping/{topping_id}")
    Call<ApiResponse<Void>> updateTopping(@Path("group_id") String groupId,
                                             @Path("topping_id") String toppingId,
                                             @Body Topping topping);

    // Remove a topping from a specific topping group
    @DELETE("/api/v1/store/topping-group/{group_id}/topping/{topping_id}")
    Call<ApiResponse<Void>> removeToppingFromGroup(@Path("group_id") String groupId,
                                                   @Path("topping_id") String toppingId);

    // Delete a topping group
    @DELETE("/api/v1/store/topping-group/{group_id}")
        Call<ApiResponse<Void>> deleteToppingGroup(@Path("group_id") String groupId);

    @POST("/api/v1/store/{store_id}/topping-group/add")
    Call<ApiResponse<Void>> addToppingGroup(@Body ToppingGroup toppingGroup, @Path("store_id") String storeId);
}

