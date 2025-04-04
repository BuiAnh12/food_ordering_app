package com.example.food_ordering_mobile_app.ui.orders;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.LatestOrderAdapter;
import com.example.food_ordering_mobile_app.models.order.OrderItem;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.ui.MainStoreActivity;
import com.example.food_ordering_mobile_app.ui.common.LoginActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.AuthViewModel;
import com.example.food_ordering_mobile_app.viewModel.OrderViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FragmentLatestOrder extends Fragment {
    private LatestOrderAdapter latestOrderAdapter;
    private List<Order> orderList =  new ArrayList<>();
    private RecyclerView latestOrderRecyclerView;


    private OrderViewModel orderViewModel;

    public FragmentLatestOrder() {
        // Required empty public constructor
    }

    public static FragmentLatestOrder newInstance(String param1, String param2) {
        FragmentLatestOrder fragment = new FragmentLatestOrder();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_latest_order, container, false);

        // Khởi tạo RecyclerView
        latestOrderRecyclerView = view.findViewById(R.id.latestOrderRecyclerView);
        latestOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Gán Adapter ngay từ đầu (danh sách rỗng)
        latestOrderAdapter = new LatestOrderAdapter(getContext(), orderList);
        latestOrderRecyclerView.setAdapter(latestOrderAdapter);

        // Khởi tạo ViewModel
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        // Lấy danh sách đơn hàng
        orderViewModel.getAllOrders("pending", 10, 1);

        // Quan sát dữ liệu
        orderViewModel.getAllOrderResponse().observe(getViewLifecycleOwner(), new Observer<Resource<List<Order>>>() {
            @Override
            public void onChanged(Resource<List<Order>> listResource) {
                switch (listResource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (listResource.getData() != null) {
                            orderList.clear();
                            Log.e("FragmentLatestOrder", "Data: " + Arrays.toString(listResource.getData().toArray()));
                            orderList.addAll(listResource.getData());  // Cập nhật danh sách đơn hàng
                            latestOrderAdapter.notifyDataSetChanged(); // Thông báo Adapter cập nhật
                        }
                        break;
                    case ERROR:
                        Log.e("FragmentLatestOrder", "Error: " + listResource.getMessage());
                        Toast.makeText(getContext(), listResource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        return view;
    }


}