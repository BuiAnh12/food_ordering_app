package com.example.food_ordering_mobile_app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.repository.StoreDetailRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.List;

public class StoreDetailViewModel extends AndroidViewModel {
    private final StoreDetailRepository storeDetailRepository;



    private final MutableLiveData<Resource<Store>> getStoreResponse = new MutableLiveData<>();

    public StoreDetailViewModel(@NonNull Application application) {
        super(application);
        this.storeDetailRepository = new StoreDetailRepository(application);
    }

    public LiveData<Resource<Store>> getStoreResponse() {
        return getStoreResponse;
    }

    public void getStore() {
        LiveData<Resource<Store>> result = storeDetailRepository.getStore();
        result.observeForever(getStoreResponse::setValue);
    }

    private final MutableLiveData<Resource> setStoreResponse = new MutableLiveData<>();
    public void setStore(Store store) {
        LiveData<Resource> result = storeDetailRepository.setStore(store);
        result.observeForever(setStoreResponse::setValue);
    }
    public LiveData<Resource> getSetStoreResponse() {
        return setStoreResponse;
    }

}
