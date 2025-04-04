package com.example.food_ordering_mobile_app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.topping.Topping;
import com.example.food_ordering_mobile_app.models.topping.ToppingGroup;
import com.example.food_ordering_mobile_app.repository.ToppingRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.List;

public class ToppingViewModel extends AndroidViewModel {
    private final ToppingRepository toppingRepository;

    public ToppingViewModel(@NonNull Application application) {
        super(application);
        this.toppingRepository = new ToppingRepository(application);
    }
    private final MutableLiveData<Resource<List<ToppingGroup>>> getAllToppingsResponse = new MutableLiveData<>();
    public MutableLiveData<Resource<List<ToppingGroup>>> getAlllToppingsResponse() {
        return getAllToppingsResponse;
    }
    public void getAllToppings(int limit, int page) {
        LiveData<Resource<List<ToppingGroup>>> result = toppingRepository.getAllToppings(limit, page);
        result.observeForever(getAllToppingsResponse::setValue);
    }
}
