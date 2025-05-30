package com.example.food_ordering_mobile_app.ui.staffs;

import static com.example.food_ordering_mobile_app.R.*;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.StoreDetailViewModel;

import java.util.ArrayList;

public class FragmentAddStaffs extends Fragment {

    private StoreDetailViewModel staffViewModel;

    public FragmentAddStaffs() {
        // Required empty public constructor
    }

    public static FragmentAddStaffs newInstance() {
        return new FragmentAddStaffs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(layout.fragment_add_staffs, container, false);

        TextView backBtn = view.findViewById(R.id.btnReturn);
        backBtn.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        Button saveBtn = view.findViewById(R.id.buttonSave);
        saveBtn.setOnClickListener(this::createStaff);

        staffViewModel = new ViewModelProvider(this).get(StoreDetailViewModel.class);

        staffViewModel.getCreateStaffResponse().observe(getViewLifecycleOwner(), staffResource -> {
            switch (staffResource.getStatus()) {
                case SUCCESS:
                    Toast.makeText(getContext(), "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                    break;
                case ERROR:
                    Toast.makeText(getContext(), "Thêm nhân viên thất bại: " + staffResource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case LOADING:
                    // Optional loading UI
                    break;
            }
        });

        return view;
    }

    private void createStaff(View view) {
        EditText nameInput = getView().findViewById(R.id.editTextName);
        EditText emailInput = getView().findViewById(R.id.editTextEmail);
        EditText phoneInput = getView().findViewById(R.id.editTextPhone);
        EditText passwordInput = getView().findViewById(R.id.editTextPassword);
        Spinner spinnerGender = getView().findViewById(R.id.spinnerGender);
        Spinner spinnerRole = getView().findViewById(R.id.spinnerRole);


        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String role = spinnerRole.getSelectedItem().toString();
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        User newStaff = new User();
        newStaff.setName(name);
        newStaff.setEmail(email);
        newStaff.setPhonenumber(phone);
        newStaff.setPassword(password);
        String eng_gender = gender.equals("Nam") ? "male" : "female";
        newStaff.setGender(eng_gender);
        ArrayList<String> roles = new ArrayList<>();
        if ( role.equals("Nhân viên") ) {
            roles.add("staff");
        }
        else if ( role.equals("Quản lý") ) {
            roles.add("manager");
        }

        newStaff.setRole(roles);

        staffViewModel.createStaff(newStaff);
    }



    public void returnBack(View view) {
//        requireActivity().getSupportFragmentManager().popBackStack();
        requireActivity().onBackPressed();
    }
}
