package com.example.food_ordering_mobile_app.ui.orders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.OrderItemDetailAdapter;
import com.example.food_ordering_mobile_app.models.order.OrderDetail;

import java.util.Arrays;
import java.util.List;

public class FragmentOrderHistoryDetail extends Fragment {

    private OrderItemDetailAdapter orderItemDetailAdapter;
    private List<OrderDetail> orderDetailList;
    private RecyclerView orderDetailRecycleView;
    private String orderId;

    public FragmentOrderHistoryDetail() {
        // Required empty public constructor
    }

    public static FragmentOrderHistoryDetail newInstance(String orderId) {
        FragmentOrderHistoryDetail fragment = new FragmentOrderHistoryDetail();
        Bundle args = new Bundle();
        args.putString("ORDER_ID", orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderId = getArguments().getString("ORDER_ID");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println(orderId);
        View view = inflater.inflate(R.layout.fragment_order_history_detail, container, false);
        orderDetailRecycleView = view.findViewById(R.id.orderSummaryRecyclerView);
        orderDetailRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        orderDetailList = Arrays.asList(
                new OrderDetail(1, "Bún Bò Huế", 2, 50000),
                new OrderDetail(2, "Trà Sữa Trân Châu", 1, 30000)
        );
        orderItemDetailAdapter = new OrderItemDetailAdapter(getContext(), orderDetailList);
        orderDetailRecycleView.setAdapter(orderItemDetailAdapter);
        TextView backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        // Them logic thay doi nut bam theo input
        return view;

    }

}