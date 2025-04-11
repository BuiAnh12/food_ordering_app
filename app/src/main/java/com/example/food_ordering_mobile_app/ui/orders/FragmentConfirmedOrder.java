package com.example.food_ordering_mobile_app.ui.orders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.order.OrderConfirmedAdapter;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.models.order.OrderDateStatusGroup;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.OrderViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentConfirmedOrder extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView confirmOrderRecyclerView;
    private OrderConfirmedAdapter confirmedAdapter;
    private List<Order> orderList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private OrderViewModel orderViewModel;

    public FragmentConfirmedOrder() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_confirmed_order, container, false);
        // Khoi tao RecyclerView
        confirmOrderRecyclerView = view.findViewById(R.id.confirmOrderRecyclerView);
        confirmOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        confirmedAdapter = new OrderConfirmedAdapter(getContext(), orderList, orderViewModel, getViewLifecycleOwner());
        confirmOrderRecyclerView.setAdapter(confirmedAdapter);


        swipeRefreshLayout = view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        filterOrders("");

        return view;
    }

    public void filterOrders(String query) {
        orderList.clear();
        orderViewModel.getAllOrders("confirmed,finished", 10, 1, query);
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
                            Log.e("FragmentConfirmOrder", "Data: " + Arrays.toString(listResource.getData().toArray()));
                            orderList.addAll(listResource.getData());  // Cập nhật danh sách đơn hàng
                            confirmedAdapter.notifyDataSetChanged(); // Thông báo Adapter cập nhật
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        break;
                    case ERROR:
                        Log.e("FragmentConfirmOrder", "Error: " + listResource.getMessage());
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
