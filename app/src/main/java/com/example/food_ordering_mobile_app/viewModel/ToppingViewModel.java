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

    private final MutableLiveData<Resource<ToppingGroup>> getToppingResponse = new MutableLiveData<>();

    public MutableLiveData<Resource<ToppingGroup>> getToppingResponse() {
        return getToppingResponse;
    }

    public void getTopping(String groupId) {
        LiveData<Resource<ToppingGroup>> result = toppingRepository.getTopping(groupId);
        result.observeForever(getToppingResponse::setValue);
    }

    private final MutableLiveData<Resource<Void>> addToppingToGroupResponse = new MutableLiveData<>();
    public MutableLiveData<Resource<Void>> addToppingToGroupResponse() {
        return addToppingToGroupResponse;
    }
    public void addToppingToGroup(String groupId, Topping topping) {
        LiveData<Resource<Void>> result = toppingRepository.addToppingToGroup(groupId, topping);
        result.observeForever(addToppingToGroupResponse::setValue);
    }

    private final MutableLiveData<Resource<Void>> updateToppingResponse = new MutableLiveData<>();

    public MutableLiveData<Resource<Void>> updateToppingResponse() {
        return updateToppingResponse;
    }
    public void updateTopping(String groupId, String toppingId, Topping topping) {
        LiveData<Resource<Void>> result = toppingRepository.updateTopping(groupId, toppingId, topping);
        result.observeForever(updateToppingResponse::setValue);
    }

    private final MutableLiveData<Resource<Void>> removeToppingFromGroupResponse = new MutableLiveData<>();
    public MutableLiveData<Resource<Void>> removeToppingFromGroupResponse() {
        return removeToppingFromGroupResponse;
    }

    public void removeToppingFromGroup(String groupId, String toppingId) {
        LiveData<Resource<Void>> result = toppingRepository.removeToppingFromGroup(groupId, toppingId);
        result.observeForever(removeToppingFromGroupResponse::setValue);
    }

    private final MutableLiveData<Resource<Void>> addToppingGroupResponse = new MutableLiveData<>();

    public MutableLiveData<Resource<Void>> addToppingGroupResponse() {
        return addToppingGroupResponse;
    }

    public void addToppingGroup(ToppingGroup toppingGroup) {
        LiveData<Resource<Void>> result = toppingRepository.addToppingGroup(toppingGroup);
        result.observeForever(addToppingGroupResponse::setValue);
    }

    private final MutableLiveData<Resource<Void>> deleteToppingGroupResponse = new MutableLiveData<>();
    public MutableLiveData<Resource<Void>> deleteToppingGroupResponse() {
        return deleteToppingGroupResponse;
    }
    public void deleteToppingGroup(String groupId) {
        LiveData<Resource<Void>> result = toppingRepository.deleteToppingGroup(groupId);
        result.observeForever(deleteToppingGroupResponse::setValue);
    }

}
