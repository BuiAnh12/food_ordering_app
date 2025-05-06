package com.example.food_ordering_mobile_app.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.food_ordering_mobile_app.authorization.SecurityManager;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.ui.MainStoreActivity;
import com.example.food_ordering_mobile_app.viewModel.StoreDetailViewModel;

public class SplashActivity extends AppCompatActivity {

    private StoreDetailViewModel storeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            storeViewModel = new ViewModelProvider(this).get(StoreDetailViewModel.class);
            storeViewModel.getStore();
            storeViewModel.getStoreResponse().observe(this, data -> {
                if (data == null) return;

                switch (data.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        String status = data.getData().getStatus();
                        boolean isApproved = data.getData().isApproved();
                        Log.d("Login", "Store status: " + status);
                        Log.d("Login", "Store isApproved: " + isApproved);
                        if ("BLOCKED".equals(status)) {
                            goToBlocked();
                        }
                        else if (!isApproved){
                            Intent intent = new Intent(SplashActivity.this, WaitForApprovalActivity.class);
                            startActivity(intent);
                        }
                        else {
                            goToMainStore();
                        }
                        break;
                    case ERROR:
                        Toast.makeText(SplashActivity.this, "Không thể lấy thông tin cửa hàng.", Toast.LENGTH_SHORT).show();
                        goToLoginWithDelay();
                        break;
                }
            });
        }, 1000);
    }

    private void goToMainStore() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainStoreActivity.class);
            startActivity(intent);
            finish();
        }, 1000); // optional delay for smoother transition
    }

    private void goToBlocked() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, BlockedActivity.class);
            startActivity(intent);
            finish();
        }, 1000);
    }

    private void goToLoginWithDelay() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 2000); // Keep splash time for login
    }
}


