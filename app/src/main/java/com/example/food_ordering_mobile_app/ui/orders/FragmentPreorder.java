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
import com.example.food_ordering_mobile_app.adapters.OuterPreOrderAdapter;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.order.OrderDateGroup;
import com.example.food_ordering_mobile_app.models.order.OrderItem;
import com.example.food_ordering_mobile_app.models.order.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentPreorder extends Fragment {

    private OuterPreOrderAdapter outerPreOrderAdapter;
    private List<Order> orderlist;
    private RecyclerView preOrderRecyclerView;

    public FragmentPreorder() {
        // Required empty public constructor
    }

    public static FragmentPreorder newInstance() {
        FragmentPreorder fragment = new FragmentPreorder();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_preorder, container,false);
        preOrderRecyclerView = view.findViewById(R.id.preOrderOuterRecyclerView);
        preOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        orderlist = new ArrayList<>();

//        List<OrderItem> orderItems1 = Arrays.asList(
//                new OrderItem(1, "Bún Bò Huế", 2, 50000),
//                new OrderItem(2, "Trà Sữa Trân Châu", 1, 30000)
//        );
//
//        List<OrderItem> orderItems2 = Arrays.asList(
//                new OrderItem(3, "Phở Bò", 1, 60000),
//                new OrderItem(4, "Cà Phê Sữa Đá", 1, 25000)
//        );
//
//        List<Order> todayOrders = Arrays.asList(
//                new Order(101, 1, orderItems1, "Nguyễn Văn A", "11:30", "11:12",1.2,"12/04/2003","12/04/2003"),
//                new Order(102, 2, orderItems2, "Trần Thị B", "11:30", "11:12",1.2,"12/04/2003","12/04/2003")
//        );
//
//        List<Order> tomorrowOrders = Arrays.asList(
//                new Order(103, 1, orderItems1, "Nguyễn Văn C", "14:00", "11:12",1.2,"12/04/2003","12/04/2003"),
//                new Order(104, 2, orderItems2, "Trần Thị D", "15:00", "11:12",1.2,"12/04/2003","12/04/2003")
//        );
        List<OrderDateGroup> dateGroups = new ArrayList<>();
//        dateGroups.add(new OrderDateGroup("Hôm nay", todayOrders));
//        dateGroups.add(new OrderDateGroup("Ngày mai", tomorrowOrders));

        outerPreOrderAdapter = new OuterPreOrderAdapter(dateGroups);
        preOrderRecyclerView.setAdapter(outerPreOrderAdapter);

        return view;
    }

}