package com.example.food_ordering_mobile_app.ui.orders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.OrderItemDetailAdapter;
import com.example.food_ordering_mobile_app.models.order.OrderItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FragmentOrderDetail extends Fragment {
    private OrderItemDetailAdapter orderItemDetailAdapter;
    private List<OrderItem> orderItemList = new ArrayList<>();
    private RecyclerView orderDetailRecycleView;
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
//        orderItemList = Arrays.asList(
//                new OrderItem(1, "Bún Bò Huế", 2, 50000),
//                new OrderItem(2, "Trà Sữa Trân Châu", 1, 30000)
//        );
        orderItemDetailAdapter = new OrderItemDetailAdapter(getContext(), orderItemList);
        orderDetailRecycleView.setAdapter(orderItemDetailAdapter);

        TextView backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // Them logic thay doi nut bam theo input
        Button active_btn = view.findViewById(R.id.active_button);
        Button deactive_btn = view.findViewById(R.id.deactive_button);
        TextView title = view.findViewById(R.id.title);
        switch (status){
            case "1" :{
                active_btn.setText("Sửa đơn");
                deactive_btn.setText("Hủy đơn");
                title.setText("Đặt trước");
                break;
            }
            case "2" :{
                active_btn.setText("Sửa đơn");
                deactive_btn.setText("Hủy đơn");
                title.setText("Chi tiết đơn hàng");
                break;
            }
            case "3" :{
                active_btn.setText("Sửa/Hủy");
                deactive_btn.setText("Thông báo tài xế");
                title.setText("Chi tiết đơn hàng");
                break;
            }
        }

        return view;
    }


}