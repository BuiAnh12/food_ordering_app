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
    private final Fragment[] fragments = new Fragment[3];

    public OrderAdapter(@NonNull Fragment fragment) {
        super(fragment);
        fragments[0] = new FragmentLatestOrder();
        fragments[1] = new FragmentConfirmedOrder();
        fragments[2] = new FragmentHistoryOrder();
    }
    public OrderAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    public OrderAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public Fragment getFragment(int currentItem) {
        return fragments[currentItem];
    }
}
