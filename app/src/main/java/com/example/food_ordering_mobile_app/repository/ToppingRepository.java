package com.example.food_ordering_mobile_app.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.topping.Topping;
import com.example.food_ordering_mobile_app.models.topping.ToppingGroup;
import com.example.food_ordering_mobile_app.network.RetrofitClient;
import com.example.food_ordering_mobile_app.network.services.ToppingService;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ToppingRepository {
    private ToppingService toppingService;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public ToppingRepository(Context context) {
        toppingService = RetrofitClient.getClient(context).create(ToppingService.class);
        sharedPreferencesHelper = new SharedPreferencesHelper(context);
    }

    public LiveData<Resource<List<ToppingGroup>>> getAllToppings(int limit, int page) {
        MutableLiveData<Resource<List<ToppingGroup>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null)); // Set loading state before making the request
        String storeId = sharedPreferencesHelper.getStoreId();
        toppingService.getAllToppings(storeId, limit, page).enqueue(new Callback<ApiResponse<List<ToppingGroup>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ToppingGroup>>> call, Response<ApiResponse<List<ToppingGroup>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success("Lấy danh sách món ăn thành công!", response.body().getData())); // Set successful response
                } else {
                    liveData.setValue(Resource.error("Failed to load toppings", null)); // Handle failure
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ToppingGroup>>> call, Throwable t) {
                liveData.setValue(Resource.error("Network error: " + t.getMessage(), null)); // Handle failure
            }
        });

        return liveData;
    }

}
