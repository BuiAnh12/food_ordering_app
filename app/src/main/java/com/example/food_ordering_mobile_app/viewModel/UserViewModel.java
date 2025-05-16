package com.example.food_ordering_mobile_app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.repository.UserRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import lombok.Getter;

@Getter
public class UserViewModel extends AndroidViewModel {
    private final UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.userRepository = new UserRepository(application);
    }

    final MutableLiveData<Resource<User>> currentUserResponse = new MutableLiveData<>();

    public void getCurrentUser() {
        LiveData<Resource<User>> result = userRepository.getCurrentUser();
        result.observeForever(currentUserResponse::setValue);
    }

    final MutableLiveData<Resource<User>> updateUserResponse = new MutableLiveData<>();
    public void updateUser(User user) {
        LiveData<Resource<User>> result = userRepository.updateUser(user);
        result.observeForever(updateUserResponse::setValue);
    }



}
