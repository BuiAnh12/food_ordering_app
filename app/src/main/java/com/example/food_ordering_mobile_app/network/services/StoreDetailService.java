package com.example.food_ordering_mobile_app.network.services;
import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.user.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface StoreDetailService {
    @GET("store/{store_id}")
    Call<ApiResponse<Store>> getStore(@Path("store_id") String storeId);

    @PUT("store/{store_id}")
    Call<ApiResponse<Store>> setStore(@Path("store_id") String storeId, @Body Store store);

    // Manage staff in store
    @GET("store/{store_id}/staff")
    Call<List<User>> getStaffList(@Path("store_id") String storeId);

    @PUT("store/{store_id}/staff/update")
    Call<User> updateStaff(@Path("store_id") String storeId, @Body User staff);

    @POST("store/{store_id}/staff/add")
    Call<Void> createStaff(@Path("store_id") String storeId, @Body User staff);

    @GET("store/{store_id}/staff/{staff_id}")
    Call<User> getStaff(@Path("store_id") String storeId, @Path("staff_id") String staffId);

    @DELETE("store/{store_id}/staff/{staff_id}")
    Call<Void> deleteStaff(@Path("store_id") String storeId, @Path("staff_id") String staffId);

}
