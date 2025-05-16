package com.example.food_ordering_mobile_app.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.ui.common.LoginActivity;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.AuthViewModel;
import com.example.food_ordering_mobile_app.viewModel.UserViewModel;

public class ProfileActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;
    private Intent intent;
    private User currentUser;
    private EditText nameInput, phoneInput;
    private TextView emailInput;
    private EditText oldPasswordInput, newPasswordInput, confirmPasswordInput;
    private Button updateProfileButton, updatePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_acitivity);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // Initialize UI elements
        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        emailInput = findViewById(R.id.emailInput);
        oldPasswordInput = findViewById(R.id.oldPasswordInput);
        newPasswordInput = findViewById(R.id.newPasswordInput);
        confirmPasswordInput = findViewById(R.id.confirmNewPasswordInput);
        updateProfileButton = findViewById(R.id.changeInfoBtn);
        updatePasswordButton = findViewById(R.id.changePasswordBtn);
        TextView btnLogout = findViewById(R.id.logoutBtn);
        TextView btnGoBack = findViewById(R.id.backBtn);
        authViewModel.getLogoutResponse().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        // Navigate to login on successful logout
                        intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case ERROR:
                        // Optionally show an error message here
                        Log.e("ProfileActivity", "Error logging out: " + resource.getMessage());
                        intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });
        // Load user data
        loadUserData();

        btnLogout.setOnClickListener(view -> authViewModel.logout(view.getContext()));
        btnGoBack.setOnClickListener(view -> finish());

        // Observe logout response
        authViewModel.getLogoutResponse().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case ERROR:
                        Toast.makeText(ProfileActivity.this, resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        updateProfileButton.setOnClickListener(view -> updateUserProfile());
        updatePasswordButton.setOnClickListener(view -> updatePassword());
    }

    private void loadUserData() {
        userViewModel.getCurrentUser();
        userViewModel.getCurrentUserResponse().observe(this, resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    currentUser = resource.getData();
                    nameInput.setText(currentUser.getName());
                    phoneInput.setText(currentUser.getPhonenumber());
                    emailInput.setText(currentUser.getEmail());
                case ERROR:
                    Log.e("ProfileActivity", "Error loading user data: " + resource.getMessage());
                    break;
            }
        });
    }

    private void updateUserProfile() {
        String newName = nameInput.getText().toString().trim();
        String newPhone = phoneInput.getText().toString().trim();

        if (newName.isEmpty() || newPhone.isEmpty()) {
            Toast.makeText(this, "Tên và số điện thoại không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.setName(newName);
        currentUser.setPhonenumber(newPhone);
        userViewModel.updateUser(currentUser);

        userViewModel.getUpdateUserResponse().observe(this, resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    Toast.makeText(this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    Toast.makeText(this, resource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void updatePassword() {
        String oldPassword = oldPasswordInput.getText().toString().trim();
        String newPassword = newPasswordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ các trường mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu mới và xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        authViewModel.changePassword(oldPassword, newPassword);

        authViewModel.getChangePasswordResponse().observe(this, resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    oldPasswordInput.setText("");
                    newPasswordInput.setText("");
                    confirmPasswordInput.setText("");
                    break;
                case ERROR:
                    Toast.makeText(this, resource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}
