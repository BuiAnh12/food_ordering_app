package com.example.food_ordering_mobile_app.adapters.order;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.food_ordering_mobile_app.ui.orders.FragmentConfirmedOrder;
import com.example.food_ordering_mobile_app.ui.orders.FragmentHistoryOrder;
import com.example.food_ordering_mobile_app.ui.orders.FragmentLatestOrder;

public class OrderAdapter extends FragmentStateAdapter {
    public OrderAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public OrderAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public OrderAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        switch (position) {
            case 0:
                return new FragmentLatestOrder();
            case 1:
                return new FragmentConfirmedOrder();
            case 2:
                return new FragmentHistoryOrder();
            default:
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
