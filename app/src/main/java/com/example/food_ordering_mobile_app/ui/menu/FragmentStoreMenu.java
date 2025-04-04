package com.example.food_ordering_mobile_app.ui.menu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.menu.MenuAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FragmentStoreMenu extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private MenuAdapter menuAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_menu, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        menuAdapter = new MenuAdapter(this);
        viewPager.setAdapter(menuAdapter);

        // Add tabs dynamically to ensure they are evenly spread
        tabLayout.addTab(tabLayout.newTab().setText(R.string.dishes));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.topping));


        // Set up ViewPager with TabLayout
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText(R.string.dishes); break;
                case 1: tab.setText(R.string.topping); break;
            }
        }).attach();

        return view;
    }
}