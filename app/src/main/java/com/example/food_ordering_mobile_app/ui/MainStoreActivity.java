package com.example.food_ordering_mobile_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.network.SocketManager;
import com.example.food_ordering_mobile_app.ui.chat.FragmentChat;
import com.example.food_ordering_mobile_app.ui.common.BlockedActivity;
import com.example.food_ordering_mobile_app.ui.discount.DiscountFragment;
import com.example.food_ordering_mobile_app.ui.home.HomeFragment;
import com.example.food_ordering_mobile_app.ui.orders.FragmentStoreOrder;
import com.example.food_ordering_mobile_app.ui.store.StoreDetailFragment;
import com.example.food_ordering_mobile_app.utils.Notificaiton;
import com.example.food_ordering_mobile_app.viewModel.StoreDetailViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.example.food_ordering_mobile_app.databinding.ActivityMainStoreBinding;
import com.example.food_ordering_mobile_app.authorization.SecurityManager;

import java.util.Arrays;


public class    MainStoreActivity extends AppCompatActivity {

    private ActivityMainStoreBinding binding;
    private int unreadMessageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BottomNavigationView navView = findViewById(R.id.nav_view_store);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_store_main);

        Intent intent = getIntent();
        handleIntent(intent, navView);

        NavigationUI.setupWithNavController(navView, navController);

        navView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.home_item) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.orders_item) {
                selectedFragment = new FragmentStoreOrder();
            } else if (item.getItemId() == R.id.messages_item) {
                selectedFragment = new FragmentChat();
                unreadMessageCount = 0;
                binding.navViewStore.removeBadge(R.id.messages_item);
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_store_main, selectedFragment)
                        .commit();
            }
            return true;
        });


        SocketManager.setOnStoreMessageReceivedListener(args -> runOnUiThread(() -> {
            Log.d("Socket", "messageReceived triggered with args: " + Arrays.toString(args));
            unreadMessageCount++;
            BadgeDrawable badge = binding.navViewStore.getOrCreateBadge(R.id.messages_item);
            badge.setVisible(true);
            badge.setNumber(unreadMessageCount);
        }));


    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        BottomNavigationView navView = findViewById(R.id.nav_view_store);
        handleIntent(intent, navView);
    }

    private void handleIntent(Intent intent, BottomNavigationView navView) {
        if (intent != null) {
            // Set the selected item
            if (intent.hasExtra("menu_item_id")) {
                int menuItemId = intent.getIntExtra("menu_item_id", R.id.orders_item);
                navView.setSelectedItemId(menuItemId);
            }

            // Open the target fragment if specified
            if (intent.hasExtra(Notificaiton.FRAGMENT_KEY)) {
                String fragmentName = intent.getStringExtra(Notificaiton.FRAGMENT_KEY);
                try {
                    Fragment fragment = (Fragment) Class.forName(fragmentName).newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment_activity_store_main, fragment)
                            .commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
