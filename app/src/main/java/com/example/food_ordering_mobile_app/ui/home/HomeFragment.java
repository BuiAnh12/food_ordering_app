package com.example.food_ordering_mobile_app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.food_ordering_mobile_app.R;
//import com.example.food_ordering_mobile_app.ui.cart.CartActivity;
//import com.example.food_ordering_mobile_app.ui.notifications.NotificationActivity;
import com.example.food_ordering_mobile_app.ui.menu.FragmentStoreMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {

    private ImageButton goToNotificationBtn, goToCartBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_home, container, false);

        // Initialize buttons
        goToNotificationBtn = view.findViewById(R.id.goToNotificationBtn);
        goToCartBtn = view.findViewById(R.id.goToCartBtn);

        // Set up click listeners
        goToNotificationBtn.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), NotificationActivity.class);
//            startActivity(intent);
        });

        goToCartBtn.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), CartActivity.class);
//            startActivity(intent);
        });


        LinearLayout orderLayout = view.findViewById(R.id.orderLayout);
        orderLayout.setOnClickListener(this::orderClick);

        LinearLayout menuLayout = view.findViewById(R.id.menuLayout);
        menuLayout.setOnClickListener(this::menuClick);

        return view;
    }

    public void orderClick(View view) {
        BottomNavigationView navView = requireActivity().findViewById(R.id.nav_view_store);
        navView.setSelectedItemId(R.id.orders_item); // ✅ Triggers navigation correctly
    }



    public void menuClick(View view) {
        FragmentStoreMenu fragment = new FragmentStoreMenu(); // Create an instance of your fragment
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.nav_host_fragment_activity_store_main, fragment); // Replace with your container ID
        transaction.addToBackStack(null); // Add to back stack for navigation
        transaction.commit();
    }

    public void storeInfoClick(View view) {
    }

    public void serviceQualityClick(View view) {
    }

    public void staffManagementClick(View view) {
    }

    public void helpCenterClick(View view) {
    }

}
