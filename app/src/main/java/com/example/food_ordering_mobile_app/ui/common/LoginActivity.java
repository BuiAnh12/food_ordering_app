package com.example.food_ordering_mobile_app.ui.common;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.ui.MainStoreActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.AuthViewModel;
import com.example.food_ordering_mobile_app.viewModel.StoreDetailViewModel;

public class LoginActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private EditText passwordEditText, emailEditText;
    private ImageButton toggleShowPasswordButton;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi  tạo UI
        passwordEditText = findViewById(R.id.txtPassword);
        emailEditText = findViewById(R.id.txtEmail);
        toggleShowPasswordButton = findViewById(R.id.btnShowPassword);
        btnLogin = findViewById(R.id.btn_login);

        // Toggle mật khẩu
        toggleShowPasswordButton.setOnClickListener(v -> togglePasswordVisibility(passwordEditText, toggleShowPasswordButton));

        // Khởi tạo AuthViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Quan sát kết quả đăng nhập
        authViewModel.getLoginResponse().observe(this, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        User user = resource.getData();
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        // Check store status
                        StoreDetailViewModel storeViewModel = new ViewModelProvider(LoginActivity.this).get(StoreDetailViewModel.class);
                        storeViewModel.getStore();
                        storeViewModel.getStoreResponse().observe(LoginActivity.this, data -> {
                            switch (data.getStatus()) {
                                case LOADING:
                                    break;
                                case SUCCESS:
                                    String status = data.getData().getStatus();
                                    boolean isApproved = data.getData().isApproved();
                                    Log.d("Login", "Store status: " + status);
                                    Log.d("Login", "Store isApproved: " + isApproved);
                                    if ("BLOCKED".equals(status)) {
                                        Intent intent = new Intent(LoginActivity.this, BlockedActivity.class);
                                        startActivity(intent);
                                    }
                                    else if (!isApproved){
                                        Intent intent = new Intent(LoginActivity.this, WaitForApprovalActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        Intent intent = new Intent(LoginActivity.this, MainStoreActivity.class);
                                        startActivity(intent);
                                    }

                                    finish();
                                    break;
                                case ERROR:
                                    Toast.makeText(LoginActivity.this, "Không thể lấy thông tin cửa hàng.", Toast.LENGTH_SHORT).show();
                                    break;
                            }

                        });
                        break;
                    case ERROR:
                        Toast.makeText(LoginActivity.this, resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        // Xử lý đăng nhập bằng email
        btnLogin.setOnClickListener(view -> handleLogin());
    }

    private void handleLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("Login", "Email: " + email + ", Password: " + password);
        authViewModel.login(email, password);
    }

    private void togglePasswordVisibility(EditText editText, ImageButton toggleButton) {
        boolean isCurrentlyVisible = editText.getTransformationMethod() == null;
        if (isCurrentlyVisible) {
            editText.setTransformationMethod(new PasswordTransformationMethod());
            toggleButton.setImageResource(R.drawable.ic_eye_hide_24);
        } else {
            editText.setTransformationMethod(null);
            toggleButton.setImageResource(R.drawable.ic_eye_show_24);
        }
        editText.setSelection(editText.getText().length());
    }

    public void redirectToRegisterPage(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void redirectToForgotPasswordPage(View view) {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
        finish();
    }

//    public void login(View view) {
//        Intent intent = new Intent(LoginActivity.this, MainStoreActivity.class);
//        startActivity(intent);
//        finish();
//    }
}
