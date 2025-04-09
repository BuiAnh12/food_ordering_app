package com.example.food_ordering_mobile_app.ui.orders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.order.ModifyOrderAdapter;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.models.order.OrderItem;
import com.example.food_ordering_mobile_app.utils.Resource;

import com.example.food_ordering_mobile_app.viewModel.DishViewModel;
import com.example.food_ordering_mobile_app.viewModel.OrderViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentModifyOrder extends Fragment {
    private RecyclerView recyclerView;
    private ModifyOrderAdapter adapter;
    private OrderViewModel orderViewModel;
    private String orderId;

    private String status;
    private Order order = new Order();

    public static FragmentModifyOrder newInstance(String orderId, String status) {
        FragmentModifyOrder fragment = new FragmentModifyOrder();
        Bundle args = new Bundle();
        args.putString("ORDER_ID", orderId);
        args.putString("STATUS", status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            orderId = getArguments().getString("ORDER_ID");
            status = getArguments().getString("STATUS");
        }

        return inflater.inflate(R.layout.fragment_modify_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.order_modify_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        adapter = new ModifyOrderAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        Button btnAdd = view.findViewById(R.id.btn_add_item);
        Button btnSave = view.findViewById(R.id.btn_save_order);

        TextView backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            requireActivity().onBackPressed(); // Navigate back
        });

        btnAdd.setOnClickListener(v -> {
            FragmentDishSelectionDialog dialog = new FragmentDishSelectionDialog(selectedDish -> {
                OrderItem newItem = new OrderItem();
                newItem.setDish(selectedDish);
                newItem.setQuantity(1);
                adapter.addNewDish(newItem);
                Log.d("FragmentModifyOrder", "Selected Dish: " + selectedDish.getName());
            });

            dialog.show(getParentFragmentManager(), "DishSelectionDialog");
        });

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.getOrder(orderId);
        orderViewModel.getOrderResponse().observe(getViewLifecycleOwner(), new Observer<Resource<Order>>() {
            @Override
            public void onChanged(Resource<Order> resource) {
                if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {
                    order = resource.getData();
                    adapter.setOrderItems(order.getItems());
                }
            }
        });



        btnSave.setOnClickListener(v -> {
            ArrayList<OrderItem> updatedItems = adapter.getModifiedItems();
            order.setItems(updatedItems);
            orderViewModel.updateOrder(orderId, order);
            orderViewModel.getUpdateOrderResponse().observe(getViewLifecycleOwner(), resource -> {
                if (resource.getStatus() == Resource.Status.SUCCESS) {
                    Log.d("FragmentModifyOrder", "Order updated successfully");
                    Toast toast = Toast.makeText(getContext(), "Cập nhật đơn hàng thành công", Toast.LENGTH_SHORT);
                    toast.show();
                    requireActivity().onBackPressed();
                }
            });
        });
    }

    public void goBack(View view) {
        requireActivity().onBackPressed(); // Navigate back
    }
}
