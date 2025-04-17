package com.example.food_ordering_mobile_app.ui.home;

import android.content.Intent;
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
import com.example.food_ordering_mobile_app.authorization.SecurityManager;
import com.example.food_ordering_mobile_app.ui.menu.FragmentStoreMenu;
import com.example.food_ordering_mobile_app.ui.notifications.NotificationActivity;
import com.example.food_ordering_mobile_app.ui.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {

    private ImageButton goToNotificationBtn, goToProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_home, container, false);

        // Initialize buttons
        goToNotificationBtn = view.findViewById(R.id.goToNotificationBtn);
        goToProfile = view.findViewById(R.id.gotToProfile);

        // Set up click listeners
        goToNotificationBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NotificationActivity.class);
            startActivity(intent);
        });

        goToProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });


        LinearLayout orderLayout = view.findViewById(R.id.orderLayout);
        LinearLayout staffLayout = view.findViewById(R.id.staffLayout);
        LinearLayout storeInformationLayout = view.findViewById(R.id.storeInformationLayout);
        LinearLayout serviceQualityLayout = view.findViewById(R.id.serviceQualityLayout);
        LinearLayout helpCenterLayout = view.findViewById(R.id.helpCenterLayout);
        LinearLayout menuLayout = view.findViewById(R.id.menuLayout);

        menuLayout.setOnClickListener(this::menuClick);
        orderLayout.setOnClickListener(this::orderClick);
        String userRole = SecurityManager.getHighestRole();



        switch (userRole) {
            case "owner":
            case "manager":
                // Owner & Manager have access to all sections, do nothing
                break;

            case "staff":
            case "user":
                // Hide sections for staff and users
                orderLayout.setVisibility(View.GONE);
                menuLayout.setVisibility(View.GONE);
                storeInformationLayout.setVisibility(View.GONE);
                serviceQualityLayout.setVisibility(View.GONE);
                staffLayout.setVisibility(View.GONE);
                helpCenterLayout.setVisibility(View.GONE);
                break;

            default:
                // Hide everything if role is unknown (optional)
                orderLayout.setVisibility(View.GONE);
                menuLayout.setVisibility(View.GONE);
                storeInformationLayout.setVisibility(View.GONE);
                serviceQualityLayout.setVisibility(View.GONE);
                staffLayout.setVisibility(View.GONE);
                helpCenterLayout.setVisibility(View.GONE);
                break;
        }



        return view;
    }

    public void orderClick(View view) {
        BottomNavigationView navView = requireActivity().findViewById(R.id.nav_view_store);
        navView.setSelectedItemId(R.id.orders_item);
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
