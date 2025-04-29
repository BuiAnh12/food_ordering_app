package com.example.food_ordering_mobile_app.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.notification.Notification;
import com.example.food_ordering_mobile_app.repository.NotificationRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class NotificationViewModel extends AndroidViewModel {
    private final NotificationRepository notificationRepository;

    private final MutableLiveData<Resource<ApiResponse<List<Notification>>>> allNotificationsResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<List<Notification>>>> getAllNotificationsResponse() {
        return allNotificationsResponse;
    }
    private final MutableLiveData<Resource<ApiResponse<List<Notification>>>> updateNotificationStatusResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<List<Notification>>>> getUpdateNotificationStatusResponse() {
        return updateNotificationStatusResponse;
    }

    private MutableLiveData<Resource<List<Notification>>> updatedNotifications  = new MutableLiveData<>();
    public LiveData<Resource<List<Notification>>> getNotificationsResponse() {
        return updatedNotifications ;
    }

    private final MutableLiveData<Resource<ApiResponse<List<Notification>>>> storeNotificationResponse = new MutableLiveData<>();
    public LiveData<Resource<ApiResponse<List<Notification>>>> getStoreNotificationResponse() {
        return storeNotificationResponse;
    }

    public NotificationViewModel(Application application) {
        super(application);
        notificationRepository = new NotificationRepository(application);
    }

    public void getAllNotifications() {
        LiveData<Resource<ApiResponse<List<Notification>>>> result = notificationRepository.getAllNotifications();
        result.observeForever(new Observer<Resource<ApiResponse<List<Notification>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Notification>>> resource) {
                Log.d("NotificationViewModel", "getAllNotifications: " + resource);
                allNotificationsResponse.setValue(resource);
            }
        });
    }

    public void updateNotificationStatus(String id) {
        LiveData<Resource<ApiResponse<List<Notification>>>> result = notificationRepository.updateNotificationStatus(id);
        result.observeForever(new Observer<Resource<ApiResponse<List<Notification>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Notification>>> resource) {
                Log.d("NotificationViewModel", "updateNotificationStatus: " + resource);
                updateNotificationStatusResponse.postValue(resource);
            }
        });
    }

    public void updateNotifications(List<Notification> notificationsList) {
        // Tạo một đối tượng List<Notification> mới (dùng ArrayList để lưu trữ dữ liệu)
        List<Notification> response = new ArrayList<>(notificationsList);

        // Cập nhật LiveData với giá trị thành công
        updatedNotifications.postValue(new Resource<>(Resource.Status.SUCCESS, response, null));
    }

    public void getStoreNotification(int limit, int page) {
        LiveData<Resource<ApiResponse<List<Notification>>>> result = notificationRepository.getStoreNotification(limit, page);
        result.observeForever(new Observer<Resource<ApiResponse<List<Notification>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Notification>>> resource) {
                Log.d("NotificationViewModel", "getStoreNotification: " + resource);
                storeNotificationResponse.setValue(resource);
            }
        });
    }

    private MutableLiveData<List<Notification>> notifications = new MutableLiveData<>();

    public void addNewNotification(Notification notification) {
        Log.d("NotificationViewModel", "addNewNotification: " + notification);
        List<Notification> currentList = notifications.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.add(0, notification);
        notifications.postValue(currentList);
    }

    public LiveData<List<Notification>> getNewNotification() {
        return notifications;
    }



}
