package com.example.food_ordering_mobile_app.network.services;
import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.store.Store;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface StoreDetailService {
    @GET("store/{store_id}")
    Call<ApiResponse<Store>> getStore(@Path("store_id") String storeId);

    @PUT("store/{store_id}")
    Call<ApiResponse<Store>> setStore(@Path("store_id") String storeId, @Body Store store);
}
