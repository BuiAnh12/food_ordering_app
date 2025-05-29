package com.example.food_ordering_mobile_app.network.services;
import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.order.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderService {
    @GET("order/store/{store_id}")
    Call<ApiResponse<List<Order>>> getAllOrders(@Path("store_id") String storeId,
                                                @Query("status") String status,
                                                @Query("limit") Integer limit,
                                                @Query("page") Integer page,
                                                @Query("name") String name);

    @GET("order/{order_id}/store")
    Call<ApiResponse<Order>> getOrder(@Path("order_id") String orderId);

    @PUT("order/{order_id}")
    Call<ApiResponse> updateOrder(@Path("order_id") String orderId, @Body Order order);
}
