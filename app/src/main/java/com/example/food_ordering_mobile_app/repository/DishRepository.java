package com.example.food_ordering_mobile_app.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.topping.Topping;
import com.example.food_ordering_mobile_app.models.topping.ToppingGroup;
import com.example.food_ordering_mobile_app.network.RetrofitClient;
import com.example.food_ordering_mobile_app.network.services.DishService;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DishRepository {
    private DishService dishService;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public DishRepository(Context context) {
        dishService = RetrofitClient.getClient(context).create(DishService.class);
        sharedPreferencesHelper = new SharedPreferencesHelper(context);
    }

    public LiveData<Resource<List<Dish>>> getAllDishes(String name, int limit, int page) {
        MutableLiveData<Resource<List<Dish>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        String storeId = sharedPreferencesHelper.getStoreId();
        Log.d("DishRepository", "Store ID: " + storeId);

        dishService.getAllDishes(storeId, name, limit, page).enqueue(new Callback<ApiResponse<List<Dish>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Dish>>> call, Response<ApiResponse<List<Dish>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success("Lấy danh sách món ăn thành công!", response.body().getData()));
                } else {
                    handleErrorResponse(response, result);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Dish>>> call, Throwable throwable) {
                handleFailure(throwable, result);
            }
        });

        return result;
    }

    public LiveData<Resource<Dish>> getDish(String dishId) {
        MutableLiveData<Resource<Dish>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        dishService.getDish(dishId).enqueue(new Callback<ApiResponse<Dish>>() {
            @Override
            public void onResponse(Call<ApiResponse<Dish>> call, Response<ApiResponse<Dish>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success("Lấy món ăn thành công!", response.body().getData()));
                } else {
                    handleErrorResponse(response, result);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Dish>> call, Throwable throwable) {
                handleFailure(throwable, result);
            }
        });
        return result;
    }

    public LiveData<Resource<Dish>> updateDish(String dishId, Dish dish) {
        MutableLiveData<Resource<Dish>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        dishService.updateDish(dishId, dish).enqueue(new Callback<ApiResponse<Dish>>() {
            @Override
            public void onResponse(Call<ApiResponse<Dish>> call, Response<ApiResponse<Dish>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success("Cập nhật món ăn thành công!", response.body().getData()));
                } else {
                    handleErrorResponse(response, result);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Dish>> call, Throwable throwable) {
                handleFailure(throwable, result);
            }
        });
        return result;
    }

    public LiveData<Resource<Dish>> createDish(Dish dish) {
        MutableLiveData<Resource<Dish>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        String storeId = sharedPreferencesHelper.getStoreId();
        dishService.createDish(storeId, dish).enqueue(new Callback<ApiResponse<Dish>>() {
            @Override
            public void onResponse(Call<ApiResponse<Dish>> call, Response<ApiResponse<Dish>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success("Tạo món ăn thành công!", response.body().getData()));
                } else {
                    handleErrorResponse(response, result);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Dish>> call, Throwable throwable) {
                handleFailure(throwable, result);
            }
        });
        return result;
    }

    public LiveData<Resource<Float>> getAvgRating(String dishId) {
        MutableLiveData<Resource<Float>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        dishService.getAvgRating(dishId).enqueue(new Callback<ApiResponse<Float>>() {
            @Override
            public void onResponse(Call<ApiResponse<Float>> call, Response<ApiResponse<Float>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success("Lấy đánh giá trung bình thành công!", response.body().getData()));
                } else {
                    handleErrorResponse(response, result);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Float>> call, Throwable throwable) {
                handleFailure(throwable, result);
            }
        });
        return result;
    }

    public LiveData<Resource<List<ToppingGroup>>> getToppingsFromDish(String dishId) {
        MutableLiveData<Resource<List<ToppingGroup>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        dishService.getToppingsFromDish(dishId).enqueue(new Callback<ApiResponse<List<ToppingGroup>>>() {

            @Override
            public void onResponse(Call<ApiResponse<List<ToppingGroup>>> call, Response<ApiResponse<List<ToppingGroup>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success("Lấy danh sách topping thành công!", response.body().getData()));
                } else {
                    handleErrorResponse(response, result);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ToppingGroup>>> call, Throwable throwable) {
                handleFailure(throwable, result);
            }
        });
        return result;
    }

    private <T> void handleErrorResponse(Response<ApiResponse<T>> response, MutableLiveData<Resource<T>> result) {
        try {
            String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Lỗi không xác định!";
            Log.e("DishRepository", "API Error: " + errorMessage);
            result.setValue(Resource.error(errorMessage, null));
        } catch (Exception e) {
            Log.e("DishRepository", "Error parsing errorBody", e);
        }
    }

    private <T> void handleFailure(Throwable throwable, MutableLiveData<Resource<T>> result) {
        Log.e("DishRepository", "Lỗi kết nối: " + throwable.getMessage(), throwable);
        result.setValue(Resource.error("Lỗi kết nối", null));
    }
}