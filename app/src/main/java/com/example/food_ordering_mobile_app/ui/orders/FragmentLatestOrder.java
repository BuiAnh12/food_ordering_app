package com.example.food_ordering_mobile_app.ui.orders;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.order.LatestOrderAdapter;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.OrderViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FragmentLatestOrder extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private LatestOrderAdapter latestOrderAdapter;
    private List<Order> orderList =  new ArrayList<>();
    private RecyclerView latestOrderRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    interface refreshListener {
        void onRefresh();
    }


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
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        // Khởi tạo RecyclerView
        latestOrderRecyclerView = view.findViewById(R.id.latestOrderRecyclerView);
        latestOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Gán Adapter ngay từ đầu (danh sách rỗng)
        latestOrderAdapter = new LatestOrderAdapter(getContext(), orderList,  new LatestOrderAdapter.OnOrderClickListener() {

            @Override
            public void onOrderClick(Order order) {

            }

            @Override
            public void onNextStatusClick(Order order) {
                order.moveToNextStatus();
                orderViewModel.updateOrder(order.getId(), order);
                orderViewModel.getUpdateOrderResponse().observe(getViewLifecycleOwner(), resource -> {
                    if (resource.getStatus() == Resource.Status.SUCCESS) {
                        Log.d("FragmentModifyOrder", "Order updated successfully");
                        Toast toast = Toast.makeText(getContext(), "Nhận đơn hàng thành công", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        });
        latestOrderRecyclerView.setAdapter(latestOrderAdapter);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        filterOrders("");


        return view;
    }

    public void filterOrders(String query) {
        if (orderViewModel == null) {
            Log.e("FragmentLatestOrder", "OrderViewModel is null. Skipping filter.");
            return;
        }
        orderList.clear();
        orderViewModel.getAllOrders("pending", 100, 1, query);
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
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        break;
                    case ERROR:
                        Log.e("FragmentLatestOrder", "Error: " + listResource.getMessage());
                        Toast.makeText(getContext(), listResource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        filterOrders("");
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof FragmentStoreOrder) {
            EditText searchEditText = ((FragmentStoreOrder) parentFragment).getSearchEditText();
            if (searchEditText != null) {
                searchEditText.setText("");
            }
        }
    }
}