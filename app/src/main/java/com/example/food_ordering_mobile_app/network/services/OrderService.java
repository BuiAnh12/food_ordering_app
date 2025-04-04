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
    @GET("/api/v1/store/{store_id}/order")
    Call<ApiResponse<List<Order>>> getAllOrders(@Path("store_id") String storeId,
                                                @Query("status") String status,
                                                @Query("limit") Integer limit,
                                                @Query("page") Integer page);

    @GET("/api/v1/store/order/{order_id}")
    Call<ApiResponse<Order>> getOrder(@Path("order_id") String orderId);

    @PUT("/api/v1/store/order/{order_id}")
    Call<ApiResponse<Order>> updateOrder(@Path("order_id") String orderId, @Body Order order);
}
