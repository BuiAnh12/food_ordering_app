package com.example.food_ordering_mobile_app.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.food_ordering_mobile_app.authorization.SecurityManager;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.ui.MainStoreActivity;

public class SplashActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SecurityManager.isLogin()) {
                    intent = new Intent(SplashActivity.this, MainStoreActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 2000);
    }
}

