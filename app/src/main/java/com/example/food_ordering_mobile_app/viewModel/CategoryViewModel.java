package com.example.food_ordering_mobile_app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.category.Category;
import com.example.food_ordering_mobile_app.repository.CategoryRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private final CategoryRepository categoryRepository;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        this.categoryRepository = new CategoryRepository(application);
    }

    private final MutableLiveData<Resource<List<Category>>> getAllCategoryResponse = new MutableLiveData<>();

    public void getAllCategories(Integer limit, Integer page, String name) {
        LiveData<Resource<List<Category>>> result = categoryRepository.getAllCategories(limit, page, name);
        result.observeForever(getAllCategoryResponse::setValue);
    }

    public LiveData<Resource<List<Category>>> getAllCategoryResponse() {
        return getAllCategoryResponse;
    }

}
