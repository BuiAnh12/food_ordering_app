package com.example.food_ordering_mobile_app.repository;

import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.network.RetrofitClient;
import com.example.food_ordering_mobile_app.network.services.UserService;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private UserService userService;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public UserRepository(Context context) {
        userService = RetrofitClient.getClient(context).create(UserService.class);
        sharedPreferencesHelper = new SharedPreferencesHelper(context);
    }

    public LiveData<Resource<User>> getUserById(String userId) {
        MutableLiveData<Resource<User>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null)); // Set loading state before making the request
        userService.getUserById(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success("Lấy thông tin người dùng thành công!", response.body())); // Set successful response
                } else {
                    liveData.setValue(Resource.error("Không thể lấy thông tin người dùng", null)); // Handle failure

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                liveData.setValue(Resource.error("Lỗi kết nối", null)); // Handle failure
            }
        });
        return liveData;
    }
    public LiveData<Resource<User>> getCurrentUser() {
        String userId = sharedPreferencesHelper.getUserId();
        return getUserById(userId);
    }
    public LiveData<Resource<User>> updateUser(User user) {
        MutableLiveData<Resource<User>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null)); // Set loading state before making the request
        userService.updateUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success("Cập nhật thông tin người dùng thành công!", response.body())); // Set successful response
                } else {
                    liveData.setValue(Resource.error("Không thể cập nhật thông tin người dùng", null)); // Handle failure
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                liveData.setValue(Resource.error("Lỗi kết nối", null)); // Handle failure);
            }
        });
        return liveData;
    }
}
