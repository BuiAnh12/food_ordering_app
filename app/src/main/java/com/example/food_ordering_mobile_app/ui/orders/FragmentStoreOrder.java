package com.example.food_ordering_mobile_app.ui.orders;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.order.OrderAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FragmentStoreOrder extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private OrderAdapter orderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_order, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        orderAdapter = new OrderAdapter(this);
        viewPager.setAdapter(orderAdapter);

        // Add tabs dynamically to ensure they are evenly spread
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.preorder));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.latest));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.confirmed));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.history));


        // Set up ViewPager with TabLayout
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
//                case 0: tab.setText("Preorder"); break;
                case 0: tab.setText("Latest"); break;
                case 1: tab.setText("Confirmed"); break;
                case 2: tab.setText("History"); break;
            }
        }).attach();

        EditText searchEditText = view.findViewById(R.id.search_edit_text);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int currentItem = viewPager.getCurrentItem();
                Fragment currentFragment = orderAdapter.getFragment(currentItem);

                if (currentFragment instanceof FragmentLatestOrder) {
                    ((FragmentLatestOrder) currentFragment).filterOrders(s.toString());
                } else if (currentFragment instanceof FragmentConfirmedOrder) {
                    ((FragmentConfirmedOrder) currentFragment).filterOrders(s.toString());
                } else if (currentFragment instanceof FragmentHistoryOrder) {
                    ((FragmentHistoryOrder) currentFragment).filterOrders(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        return view;
    }
    public EditText getSearchEditText() {
        return getView() != null ? getView().findViewById(R.id.search_edit_text) : null;
    }
}
