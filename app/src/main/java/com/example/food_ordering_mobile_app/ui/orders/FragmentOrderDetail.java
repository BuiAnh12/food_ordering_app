package com.example.food_ordering_mobile_app.ui.orders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.order.OrderItemDetailAdapter;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.models.order.OrderItem;
import com.example.food_ordering_mobile_app.utils.Function;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.OrderViewModel;

import java.util.ArrayList;
import java.util.List;


public class FragmentOrderDetail extends Fragment {
    private OrderItemDetailAdapter orderItemDetailAdapter;
    private Order order = new Order();
    private RecyclerView orderDetailRecycleView;

    private OrderViewModel orderViewModel;
    private String orderId;
    private String status;

    public FragmentOrderDetail() {
        // Required empty public constructor
    }


    public static FragmentOrderDetail newInstance(String orderId, String status) {
        FragmentOrderDetail fragment = new FragmentOrderDetail();
        Bundle args = new Bundle();
        args.putString("ORDER_ID", orderId);
        args.putString("STATUS", status);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            orderId = getArguments().getString("ORDER_ID");
            status = getArguments().getString("STATUS");
        }
        System.out.println(orderId);
        System.out.println(status);
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        orderDetailRecycleView = view.findViewById(R.id.orderDetailRecycleView);
        orderDetailRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        orderItemDetailAdapter = new OrderItemDetailAdapter(getContext(), order.getItems());
        orderDetailRecycleView.setAdapter(orderItemDetailAdapter);

        TextView backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        Button modifyOrder = view.findViewById(R.id.active_button);
        modifyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentModifyOrder modifyOrderFragment = FragmentModifyOrder.newInstance(orderId, status);


                // Get FragmentManager (make sure this code runs inside an Activity or Fragment)
                FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();

                // Begin transaction and replace the container
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_store_main, modifyOrderFragment)  // Make sure R.id.fragment_container exists in your layout
                        .addToBackStack(null)  // Adds to back stack so user can go back
                        .commit();
            }
        });
        Button deactive_btn = view.findViewById(R.id.deactive_button);
        TextView title = view.findViewById(R.id.title);
        switch (status){
            case "1" :{
                modifyOrder.setText("Sửa đơn");
                deactive_btn.setText("Hủy đơn");
                title.setText("Đặt trước");
                break;
            }
            case "2" :{
                modifyOrder.setText("Sửa đơn");
                deactive_btn.setText("Hủy đơn");
                title.setText("Chi tiết đơn hàng");
                break;
            }
            case "3" :{
                modifyOrder.setText("Sửa/Hủy");
                deactive_btn.setText("Thông báo tài xế");
                title.setText("Chi tiết đơn hàng");
                break;
            }
        }

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.getOrder(orderId);

        orderViewModel.getOrderResponse().observe(getViewLifecycleOwner(), new Observer<Resource<Order>>() {
            @Override
            public void onChanged(Resource<Order> orderResource) {
                switch (orderResource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (orderResource.getData() != null) {
                            order = orderResource.getData();
                            displayOrder(order, view);
                            orderItemDetailAdapter = new OrderItemDetailAdapter(getContext(), order.getItems());
                            orderDetailRecycleView.setAdapter(orderItemDetailAdapter);
//                            orderItemDetailAdapter.notifyDataSetChanged(); // Thông báo Adapter cập nhật
                        }
                        break;
                }
            }
        });

        return view;
    }

    public void displayOrder(Order order, View view) {
        TextView tv_customer_name = view.findViewById(R.id.tv_customer_name);
        TextView tv_customer_phone = view.findViewById(R.id.tv_customer_phone);
        TextView shipper_name = view.findViewById(R.id.tvShipperName);
        TextView totalPriceBefore = view.findViewById(R.id.total_price_before);
        TextView totalPriceAfter = view.findViewById(R.id.total_price_after);
        TextView orderId = view.findViewById(R.id.order_id);
        TextView orderTime = view.findViewById(R.id.order_date_time);



        tv_customer_name.setText(order.getCustomerName() != "" ? order.getCustomerName() : order.getUser().getName());
        tv_customer_phone.setText(order.getCustomerPhonenumber() != "" ? order.getCustomerPhonenumber() : "Unknow");
        shipper_name.setText(order.getShipper() != "" ? order.getShipper() : "Chưa chỉ định tài xế");
        totalPriceBefore.setText(String.valueOf(order.getTotalPrice()));
        totalPriceAfter.setText(String.valueOf(order.getTotalPrice()));
        orderId.setText(Function.generateOrderNumber(order.getId()));
        Log.d("TAG", "displayOrder: " + order.getCreatedAt());
        orderTime.setText(Function.dateConverter(order.getCreatedAt(), "dd/MM/yyyy HH:mm"));
    }


}