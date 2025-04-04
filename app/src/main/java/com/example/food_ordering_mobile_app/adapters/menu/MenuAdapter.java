package com.example.food_ordering_mobile_app.adapters.menu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.food_ordering_mobile_app.ui.menu.FragmentDishes;
import com.example.food_ordering_mobile_app.ui.menu.FragmentTopping;


public class MenuAdapter extends FragmentStateAdapter {
    public MenuAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MenuAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public MenuAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        if (position == 1) {
            return new FragmentTopping();
        }
        else {
            return new FragmentDishes();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
