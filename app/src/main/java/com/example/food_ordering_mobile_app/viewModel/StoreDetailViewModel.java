package com.example.food_ordering_mobile_app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.user.User;
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

    private final MutableLiveData<Resource<List<User>>> getStaffListResponse = new MutableLiveData<>();
    public void getStaffList() {
        LiveData<Resource<List<User>>> result = storeDetailRepository.getStaffList();
        result.observeForever(getStaffListResponse::setValue);
    }
    public LiveData<Resource<List<User>>> getGetStaffListResponse() {
        return getStaffListResponse;
    }

    private final MutableLiveData<Resource> updateStaffResponse = new MutableLiveData<>();
    public void updateStaff(String staffId, User staff) {
        LiveData<Resource> result = storeDetailRepository.updateStaff(staffId, staff);
        result.observeForever(updateStaffResponse::setValue);
    }
    public LiveData<Resource> getUpdateStaffResponse() {
        return updateStaffResponse;
    }

    private final MutableLiveData<Resource> createStaffResponse = new MutableLiveData<>();
    public void createStaff(User staff) {
        LiveData<Resource> result = storeDetailRepository.createStaff(staff);
        result.observeForever(createStaffResponse::setValue);
    }
    public LiveData<Resource> getCreateStaffResponse() {
        return createStaffResponse;
    }

    private final MutableLiveData<Resource<User>> getStaffResponse = new MutableLiveData<>();

    public void getStaff(String staffId) {
        LiveData<Resource<User>> result = storeDetailRepository.getStaff(staffId);
        result.observeForever(getStaffResponse::setValue);
    }

    public LiveData<Resource<User>> getGetStaffResponse() {
        return getStaffResponse;
    }

    public final MutableLiveData<Resource> deleteStaffResponse = new MutableLiveData<>();
    public void deleteStaff(String staffId) {
        LiveData<Resource> result = storeDetailRepository.deleteStaff(staffId);
        result.observeForever(deleteStaffResponse::setValue);
    }

    public LiveData<Resource> getDeleteStaffResponse() {
        return deleteStaffResponse;
    }

}
