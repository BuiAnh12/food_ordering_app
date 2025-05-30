package com.example.food_ordering_mobile_app.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.network.RetrofitClient;
import com.example.food_ordering_mobile_app.network.services.StoreDetailService;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreDetailRepository {

    private StoreDetailService storeDetailService;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public StoreDetailRepository(Context context) {
        this.storeDetailService = RetrofitClient.getClient(context).create(StoreDetailService.class);
        this.sharedPreferencesHelper = new SharedPreferencesHelper(context);
    }

    public LiveData<Resource<Store>> getStore() {
        MutableLiveData<Resource<Store>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        String storeId = sharedPreferencesHelper.getStoreId();
        Log.d("StoreDetailRepository", "Store ID: " + storeId);

        storeDetailService.getStore(storeId).enqueue(new Callback<ApiResponse<Store>>() {
            @Override
            public void onResponse(Call<ApiResponse<Store>> call, Response<ApiResponse<Store>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Log.d("StoreDetailRepository", "API Response: " + response.body());
                    result.setValue(Resource.success(
                            "Lấy thông tin cửa hàng thành công!",
                            response.body().getData()));
                }
                else {
                    try {
                        String errorMessage = response.errorBody() != null ?
                                response.errorBody().string() : "Lỗi không xác định!";
                        Log.e("StoreDetailRepository", "API Error: " + errorMessage);
                        result.setValue(Resource.error(errorMessage, null));
                    }
                    catch (Exception e) {
                        Log.e("StoreDetailRepository", "Error parsing errorBody", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Store>> call, Throwable throwable) {
                Log.e("StoreDetailRepository", "Lỗi khi lấy thông tin cửa hàng: " + throwable.getMessage(), throwable);
                result.setValue(Resource.error("Lỗi kết nối", null));
            }
        });
        return result;
    }

    public LiveData<Resource> setStore(Store store) {
        MutableLiveData<Resource> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        String storeId = sharedPreferencesHelper.getStoreId();
        Log.d("StoreDetailRepository", "Store ID: " + storeId);
        storeDetailService.setStore(storeId, store).enqueue(new Callback<ApiResponse<Store>>() {
            @Override
            public void onResponse(Call<ApiResponse<Store>> call, Response<ApiResponse<Store>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("StoreDetailRepository", "API Response: " + response.body());
                    result.setValue(Resource.success(
                            "Cập nhật thông tin cửa hàng thành công!",
                            response.body().getData()
                    ));
                }
                else {
                    try {
                        String errorMessage = response.errorBody() != null ?
                                response.errorBody().string() : "Lỗi không xác định!";
                        Log.e("StoreDetailRepository", "API Error: " + errorMessage);
                        result.setValue(Resource.error(errorMessage, null));
                    }
                    catch (Exception e) {
                        Log.e("StoreDetailRepository", "Error parsing errorBody", e);
                        result.setValue(Resource.error("Lỗi kết nối", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Store>> call, Throwable throwable) {
                Log.e("StoreDetailRepository", "Lỗi khi cập nhật thông tin cửa hàng: " + throwable.getMessage(), throwable);
                result.setValue(Resource.error("Lỗi kết nối", null));
            }
        });
        return  result;
    }


    public LiveData<Resource<List<User>>> getStaffList() {
        MutableLiveData<Resource<List<User>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        String storeId = sharedPreferencesHelper.getStoreId();
        Log.d("StoreDetailRepository", "Store ID: " + storeId);
        storeDetailService.getStaffList(storeId).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("StoreDetailRepository", "API Response: " + response.body());
                    result.setValue(Resource.success(
                            "Lấy danh sách nhân viên thành công!",
                            response.body()
                    ));
                }
                else {
                    try {
                        String errorMessage = response.errorBody() != null ?
                                response.errorBody().string() : "Lỗi không xác định!";
                        Log.e("StoreDetailRepository", "API Error: " + errorMessage);
                    }
                    catch (Exception e) {
                        Log.e("StoreDetailRepository", "Error parsing errorBody", e);
                    }
                    finally {
                        result.setValue(Resource.error("Lỗi kết nối", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable throwable) {
                Log.e("StoreDetailRepository", "Lỗi khi lấy danh sách nhân viên: " + throwable.getMessage(), throwable);
                result.setValue(Resource.error("Lỗi kết nối", null));
            }
        });
        return result;
    }

    public LiveData<Resource> updateStaff(String staffId, User staff) {
        MutableLiveData<Resource> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        String storeId = sharedPreferencesHelper.getStoreId();

        storeDetailService.updateStaff(storeId, staff).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("StoreDetailRepository", "API Response: " + response.body());
                    result.setValue(Resource.success(
                            "Cập nhật thông tin nhân viên thành công!",
                            response.body()
                    ));
                }
                else {
                    try {
                        String errorMessage = response.errorBody() != null ?
                                response.errorBody().string() : "Lỗi không xác định!";
                        Log.e("StoreDetailRepository", "API Error: " + errorMessage);
                    }
                    catch (Exception e) {
                        Log.e("StoreDetailRepository", "Error parsing errorBody", e);
                    }
                    finally {
                        result.setValue(Resource.error("Lỗi kết nối", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Log.e("StoreDetailRepository", "Lỗi khi cập nhật thông tin nhân viên: " + throwable.getMessage(), throwable);
            }
        });
        return result;
    }

    public LiveData<Resource> createStaff(User staff) {
        MutableLiveData<Resource> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        String storeId = sharedPreferencesHelper.getStoreId();
        storeDetailService.createStaff(storeId, staff).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("StoreDetailRepository", "API Response: " + response.body());
                    result.setValue(Resource.success(
                            "Thêm nhân viên thành công!",
                            null
                    ));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.e("StoreDetailRepository", "Lỗi khi thêm nhân viên: " + throwable.getMessage(), throwable);
            }
        });
        return  result;
    }

    public LiveData<Resource<User>> getStaff(String staffId) {
        MutableLiveData<Resource<User>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        String storeId = sharedPreferencesHelper.getStoreId();
        storeDetailService.getStaff(storeId, staffId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("StoreDetailRepository", "API Response: " + response.body());
                    result.setValue(Resource.success(
                            "Lấy thông tin nhân viên thành công!",
                            response.body()
                    ));
                }
                else {
                    try {
                        String errorMessage = response.errorBody() != null ?
                                response.errorBody().string() : "Lỗi không xác định!";
                        Log.e("StoreDetailRepository", "API Error: " + errorMessage);
                    }
                    catch (Exception e) {
                        Log.e("StoreDetailRepository", "Error parsing errorBody", e);
                    }
                    finally {
                        result.setValue(Resource.error("Lỗi kết nối", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Log.e("StoreDetailRepository", "Lỗi khi lấy thông tin nhân viên: " + throwable.getMessage(), throwable);
            }
        });
        return result;
    }

    public LiveData<Resource> deleteStaff(String staffId) {
        MutableLiveData<Resource> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        String storeId = sharedPreferencesHelper.getStoreId();
        storeDetailService.deleteStaff(storeId, staffId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    result.setValue(Resource.success("Xóa nhân viên thành công", null));
                }
                else {
                    try {
                        String errorMessage = response.errorBody() != null ?
                                response.errorBody().string() : "Lỗi không xác định!";
                        Log.e("StoreDetailRepository", "API Error: " + errorMessage);
                    }
                    catch (Exception e) {
                        Log.e("StoreDetailRepository", "Error parsing errorBody", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                result.setValue(Resource.error("Lỗi kết nối", null));
            }
        });
        return result;
    }
}
