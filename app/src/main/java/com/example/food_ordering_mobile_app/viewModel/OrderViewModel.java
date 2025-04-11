package com.example.food_ordering_mobile_app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.repository.OrderRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {
    private final OrderRepository orderRepository;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        this.orderRepository = new OrderRepository(application);
    }

    private final MutableLiveData<Resource<List<Order>>> getAllOrderResponse = new MutableLiveData<>();
    public LiveData<Resource<List<Order>>> getAllOrderResponse() {
        return getAllOrderResponse;
    }
    public void getAllOrders(String status, Integer limit, Integer page, String query) {
        LiveData<Resource<List<Order>>> result = orderRepository.getAllOrders(status, limit, page, query);
        result.observeForever(getAllOrderResponse::setValue);
    }

    private final MutableLiveData<Resource<Order>> getOrderResponse = new MutableLiveData<>();
    public LiveData<Resource<Order>> getOrderResponse() {
        return getOrderResponse;
    }

    public void getOrder(String orderId) {
        LiveData<Resource<Order>> result = orderRepository.getOrder(orderId);
        result.observeForever(getOrderResponse::setValue);
    }

    private final MutableLiveData<Resource> updateOrderResponse = new MutableLiveData<>();
    public void updateOrder(String orderId, Order order) {
        LiveData<Resource> result  = orderRepository.updateOrder(orderId, order);
        result.observeForever(updateOrderResponse::setValue);
    }
    public LiveData<Resource> getUpdateOrderResponse() {
        return updateOrderResponse;
    }
}
