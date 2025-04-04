package com.example.food_ordering_mobile_app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.repository.DishRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.List;

public class DishViewModel extends AndroidViewModel {
    private final DishRepository dishRepository;

    public DishViewModel(@NonNull Application application) {
        super(application);
        this.dishRepository = new DishRepository(application);
    }

    // LiveData để chứa danh sách món ăn
    private final MutableLiveData<Resource<List<Dish>>> getAllDishesResponse = new MutableLiveData<>();

    public LiveData<Resource<List<Dish>>> getAllDishesResponse() {
        return getAllDishesResponse;
    }

    public void getAllDishes(String name, int limit, int page) {
        LiveData<Resource<List<Dish>>> result = dishRepository.getAllDishes(name, limit, page);
        result.observeForever(getAllDishesResponse::setValue);
    }

    // LiveData để chứa thông tin một món ăn cụ thể
    private final MutableLiveData<Resource<Dish>> getDishResponse = new MutableLiveData<>();

    public LiveData<Resource<Dish>> getDishResponse() {
        return getDishResponse;
    }

    public void getDish(String dishId) {
        LiveData<Resource<Dish>> result = dishRepository.getDish(dishId);
        result.observeForever(getDishResponse::setValue);
    }

    // LiveData để cập nhật món ăn
    private final MutableLiveData<Resource<Dish>> updateDishResponse = new MutableLiveData<>();

    public LiveData<Resource<Dish>> getUpdateDishResponse() {
        return updateDishResponse;
    }

    public void updateDish(String dishId, Dish dish) {
        LiveData<Resource<Dish>> result = dishRepository.updateDish(dishId, dish);
        result.observeForever(updateDishResponse::setValue);
    }

    // LiveData để tạo món ăn mới
    private final MutableLiveData<Resource<Dish>> createDishResponse = new MutableLiveData<>();

    public LiveData<Resource<Dish>> getCreateDishResponse() {
        return createDishResponse;
    }

    public void createDish(Dish dish) {
        LiveData<Resource<Dish>> result = dishRepository.createDish(dish);
        result.observeForever(createDishResponse::setValue);
    }

    // LiveData để lấy đánh giá trung bình của món ăn
    private final MutableLiveData<Resource<Float>> avgRatingResponse = new MutableLiveData<>();

    public LiveData<Resource<Float>> getAvgRatingResponse() {
        return avgRatingResponse;
    }

    public void getAvgRating(String dishId) {
        LiveData<Resource<Float>> result = dishRepository.getAvgRating(dishId);
        result.observeForever(avgRatingResponse::setValue);
    }
}
