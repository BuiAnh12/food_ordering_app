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
public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    public AuthViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    private final MutableLiveData<Resource<User>> loginResponse = new MutableLiveData<>();
    public LiveData<Resource<User>> getLoginResponse() {
        return loginResponse;
    }

    public MutableLiveData<Resource<String>> logoutResponse = new MutableLiveData<Resource<String>>();
    public LiveData<Resource<String>> getLogoutResponse() {
        return logoutResponse;
    }
    public MutableLiveData<Resource<Store>> ownStoreResponse = new MutableLiveData<Resource<Store>>();

    public LiveData<Resource<Store>> getOwnStoreResponse() {return ownStoreResponse; }

    public void login(String email, String password) {
        Log.d("AuthViewModel", "Starting login with email: " + email);
        LiveData<Resource<User>> result = authRepository.login(email, password);
        result.observeForever(new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> resource) {
                Log.d("AuthViewModel", "Login result: " + resource.getStatus());
                if (resource.getStatus() == Resource.Status.SUCCESS) {
                    Log.d("AuthViewModel", "User data: " + resource.getData());
                } else if (resource.getStatus() == Resource.Status.ERROR) {
                    Log.e("AuthViewModel", "Error: " + resource.getMessage());
                }
                loginResponse.setValue(resource);
            }
        });
    }
    public void logout(Context context) {
        LiveData<Resource<String>> result = authRepository.logout(context);
        result.observeForever(new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                logoutResponse.setValue(resource);
            }
        });
    }

    public void getOwnStore() {
        LiveData<Resource<Store>> result = authRepository.getOwnStore();
        result.observeForever(new Observer<Resource<Store>>() {
            @Override
            public void onChanged(Resource<Store> resource) {
                ownStoreResponse.setValue(resource);
            }
        });
    }

}
