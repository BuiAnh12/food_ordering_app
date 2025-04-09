package com.example.food_ordering_mobile_app.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.network.RetrofitClient;
import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.network.services.OrderService;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {
    private OrderService orderService;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public OrderRepository(Context context) {
        orderService = RetrofitClient.getClient(context).create(OrderService.class);
        sharedPreferencesHelper = new SharedPreferencesHelper(context);
    }

    public LiveData<Resource<List<Order>>> getAllOrders(String status, Integer limit, Integer page) {
        MutableLiveData<Resource<List<Order>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null)); // Trạng thái Loading

        String storeId = sharedPreferencesHelper.getStoreId();
        Log.d("OrderRepository", "Store ID: " + storeId);

        orderService.getAllOrders(storeId, status, limit, page)
                .enqueue(new Callback<ApiResponse<List<Order>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<Order>>> call, Response<ApiResponse<List<Order>>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            Log.d("OrderRepository", "API Response: " + response.body());
                            result.setValue(Resource.success(
                                    "Lấy danh sách đơn hàng thành công!",
                                    response.body().getData()));
                        } else {
                            try {
                                String errorMessage = response.errorBody() != null ?
                                        response.errorBody().string() : "Lỗi không xác định!";
                                Log.e("OrderRepository", "API Error: " + errorMessage);
                                result.setValue(Resource.error(errorMessage, null));
                            } catch (Exception e) {
                                Log.e("OrderRepository", "Error parsing errorBody", e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<Order>>> call, Throwable throwable) {
                        Log.e("OrderRepository", "Lỗi khi lấy danh sách đơn hàng: " + throwable.getMessage(), throwable);
                        result.setValue(Resource.error("Lỗi kết nối", null));
                    }
                });

        return result;
    }


    public LiveData<Resource<Order>> getOrder(String orderId) {
        MutableLiveData<Resource<Order>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null)); // Trạng thái Loading
        orderService.getOrder(orderId).enqueue(new Callback<ApiResponse<Order>>() {
            @Override
            public void onResponse(Call<ApiResponse<Order>> call, Response<ApiResponse<Order>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Log.d("OrderRepository", "API Response: " + response.body());
                    result.setValue(Resource.success(
                            "Lấy danh sách đơn hàng thành công!",
                            response.body().getData()));
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ?
                                response.errorBody().string() : "Lỗi không xác định!";
                        Log.e("OrderRepository", "API Error: " + errorMessage);
                        result.setValue(Resource.error(errorMessage, null));
                    } catch (Exception e) {
                        Log.e("OrderRepository", "Error parsing errorBody", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Order>> call, Throwable throwable) {
                Log.e("OrderRepository", "Lỗi khi lấy danh sách đơn hàng: " + throwable.getMessage(), throwable);
                result.setValue(Resource.error("Lỗi kết nối", null));
            }
        });
        return result;

    }

    public LiveData<Resource> updateOrder(String orderId, Order order) {
        MutableLiveData<Resource> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null)); // Trạng thái Loading
        orderService.updateOrder(orderId, order).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() ) {
                    result.setValue(Resource.success(
                            "Cập nhật đơn hàng thành công!",
                            response.body().getData()
                    ));
                    Log.d("OrderRepository", "API Response: " + response.body());
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ?
                                response.errorBody().string() : "Lỗi không xác định!";
                        Log.e("OrderRepository", "API Error: " + errorMessage);
                        result.setValue(Resource.error(errorMessage, null));
                    } catch (Exception e) {
                        Log.e("OrderRepository", "Error parsing errorBody", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                Log.e("OrderRepository", "Lỗi khi lấy danh sách đơn hàng: " + throwable.getMessage(), throwable);
                result.setValue(Resource.error("Lỗi kết nối", null));
            }
        });
        return result;
    }

}
