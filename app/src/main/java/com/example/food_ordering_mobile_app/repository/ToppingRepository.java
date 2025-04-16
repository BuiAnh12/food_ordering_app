package com.example.food_ordering_mobile_app.repository;

import android.content.Context;
import android.util.Log;

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


    public LiveData<Resource<ToppingGroup>> getTopping(String toppingId) {
        MutableLiveData<Resource<ToppingGroup>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null)); // Set loading state before making the request

        toppingService.getTopping(toppingId).enqueue(new Callback<ApiResponse<ToppingGroup>>() {
            @Override
            public void onResponse(Call<ApiResponse<ToppingGroup>> call, Response<ApiResponse<ToppingGroup>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success("Lấy món ăn thành công!", response.body().getData())); // Set successful response
                }
                else {
                    liveData.setValue(Resource.error("Không thể lấy món ăn", null)); // Handle failure
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ToppingGroup>> call, Throwable t) {
                liveData.setValue(Resource.error("Lỗi kết nối", null)); // Handle failure
                Log.d("ToppingRepository", "onFailure:" + t.getMessage());
            }
        });
        return  liveData;
    }

    public LiveData<Resource<Void>> addToppingToGroup(String groupId, Topping topping) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null)); // Set loading state before making the request
        toppingService.addToppingToGroup(groupId, topping).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(Resource.success("Thêm topping thành công!", response.body().getData())); // Set successful response
                }
                else {
                    liveData.setValue(Resource.error("Không thể thêm topping", null)); // Handle failure
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                liveData.setValue(Resource.error("Lỗi kết nối", null)); // Handle failure
            }
        });
        return liveData;
    }

    public LiveData<Resource<Void>> updateTopping(String groupId, String toppingId, Topping topping) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null)); // Set loading state before making the request

        toppingService.updateTopping(groupId, toppingId, topping).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(Resource.success("Cập nhật topping thành công!", null)); // Set successful response
                }
                else {
                    liveData.setValue(Resource.error("Không thể cập nhật topping", null)); // Handle failure
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                liveData.setValue(Resource.error("Lỗi kết nối", null)); // Handle failure
            }
        });
        return liveData;
    }

    public LiveData<Resource<Void>> deleteToppingGroup(String groupId) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null)); // Set loading state before making the request
        toppingService.deleteToppingGroup(groupId).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(Resource.success("Xóa topping thành công!", null)); // Set successful response
                }
                else {
                    liveData.setValue(Resource.error("Không thể xóa topping", null)); // Handle failure
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable throwable) {
                liveData.setValue(Resource.error("Lỗi kết nối", null)); // Handle failure
            }
        });
        return liveData;
    }

    public LiveData<Resource<Void>> removeToppingFromGroup(String groupId, String toppingId) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null)); // Set loading state before making the request
        toppingService.removeToppingFromGroup(groupId,toppingId).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(Resource.success("Xóa topping thành công!", null)); // Set successful response
                }
                else {
                    liveData.setValue(Resource.error("Không thể xóa topping", null)); // Handle failure
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable throwable) {
                liveData.setValue(Resource.error("Lỗi kết nối", null)); // Handle failure
            }
        });
        return liveData;
    }

    public LiveData<Resource<Void>> addToppingGroup(ToppingGroup toppingGroup) {
        MutableLiveData<Resource<Void>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null)); // Set loading state before making the request
        String storeId = sharedPreferencesHelper.getStoreId();
        toppingService.addToppingGroup(toppingGroup, storeId).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(Resource.success("Thêm topping thành công!", null)); // Set successful response
                }
                else {
                    liveData.setValue(Resource.error("Không thể thêm topping", null)); // Handle failure
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable throwable) {
                liveData.setValue(Resource.error("Lỗi kết nối", null)); // Handle failure
            }
        });
        return liveData;

    }


}
