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
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.order.OuterHistoryAdapter;
import com.example.food_ordering_mobile_app.models.order.OrderDateStatusGroup;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.OrderViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FragmentHistoryOrder extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OuterHistoryAdapter outerHistoryAdapter;
    private List<OrderDateStatusGroup> orderlist = new ArrayList<>();
    private RecyclerView historyRecycleView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OrderViewModel orderViewModel;
    public FragmentHistoryOrder() {
        // Required empty public constructor
    }



    public static FragmentHistoryOrder newInstance() {
        FragmentHistoryOrder fragment = new FragmentHistoryOrder();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_history_order, container,false);
        historyRecycleView = view.findViewById(R.id.historyOrderRecyclerView);
        historyRecycleView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        outerHistoryAdapter = new OuterHistoryAdapter(orderlist);
        historyRecycleView.setAdapter(outerHistoryAdapter);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        filterOrders("");

        return view;
    }

    public void filterOrders(String query) {
        orderlist.clear();
        orderViewModel.getAllOrders("done,cancelled",100,1,query);
        // Quan sát dữ liệu
        orderViewModel.getAllOrderResponse().observe(getViewLifecycleOwner(), new Observer<Resource<List<Order>>>() {
            @Override
            public void onChanged(Resource<List<Order>> listResource) {
                switch (listResource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (listResource.getData() != null) {
                            orderlist.clear();
                            Log.d("FragmentHistoryOrder", "Data: " + Arrays.toString(listResource.getData().toArray()));
                            List<Order> orders = listResource.getData();
                            List<OrderDateStatusGroup> dateStatusGroups = OrderDateStatusGroup.groupOrdersByDateAndStatus(orders);
                            orderlist.addAll(dateStatusGroups);
                            outerHistoryAdapter.notifyDataSetChanged(); // Thông báo Adapter cập nhật
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