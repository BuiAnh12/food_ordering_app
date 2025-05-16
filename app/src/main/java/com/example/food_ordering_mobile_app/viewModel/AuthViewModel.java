package com.example.food_ordering_mobile_app.viewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.repository.AuthRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import lombok.Getter;

@Getter
public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final MutableLiveData<Resource<User>> loginResponse = new MutableLiveData<>();
    private final MutableLiveData<Resource<String>> logoutResponse = new MutableLiveData<>();
    private final MutableLiveData<Resource<Store>> ownStoreResponse = new MutableLiveData<>();
    private final MutableLiveData<Resource<String>> changePasswordResponse = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public LiveData<Resource<User>> getLoginResponse() {
        return loginResponse;
    }

    public LiveData<Resource<String>> getLogoutResponse() {
        return logoutResponse;
    }

    public LiveData<Resource<Store>> getOwnStoreResponse() {
        return ownStoreResponse;
    }

    public LiveData<Resource<String>> getChangePasswordResponse() {
        return changePasswordResponse;
    }

    public void login(String email, String password) {
        Log.d("AuthViewModel", "Starting login with email: " + email);
        authRepository.login(email, password).observeForever(resource -> {
            Log.d("AuthViewModel", "Login result: " + resource.getStatus());
            if (resource.getStatus() == Resource.Status.SUCCESS) {
                Log.d("AuthViewModel", "User data: " + resource.getData());
            } else if (resource.getStatus() == Resource.Status.ERROR) {
                Log.e("AuthViewModel", "Error: " + resource.getMessage());
            }
            loginResponse.setValue(resource);
        });
    }

    public void logout(Context context) {
        authRepository.logout(context).observeForever(resource -> {
            logoutResponse.setValue(resource);
        });
    }

    public void getOwnStore() {
        authRepository.getOwnStore().observeForever(resource -> {
            ownStoreResponse.setValue(resource);
        });
    }

    public void changePassword(String oldPassword, String newPassword) {
        authRepository.changePassword(oldPassword, newPassword).observeForever(resource -> {
            changePasswordResponse.setValue(resource);
        });
    }
}
