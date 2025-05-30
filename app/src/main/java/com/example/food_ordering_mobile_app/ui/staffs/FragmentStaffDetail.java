package com.example.food_ordering_mobile_app.ui.staffs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.viewModel.StoreDetailViewModel;

import java.util.ArrayList;
import java.util.Arrays;


public class FragmentStaffDetail extends Fragment {

    private static final String ARG_STAFF_ID = "STAFF_ID";
    private String staffId;
    private EditText editTextName, editTextEmail, editTextPhone;
    private Spinner spinnerGender, spinnerRole;
    private Button buttonSave, buttonDelete;

    private StoreDetailViewModel storeDetailViewModel;

    public FragmentStaffDetail() {
        // Required empty public constructor
    }

    public static FragmentStaffDetail newInstance(String staffId) {
        FragmentStaffDetail fragment = new FragmentStaffDetail();
        Bundle args = new Bundle();
        args.putString(ARG_STAFF_ID, staffId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            staffId = getArguments().getString(ARG_STAFF_ID);
        }
        storeDetailViewModel = new ViewModelProvider(this).get(StoreDetailViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_detail, container, false);
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        spinnerGender = view.findViewById(R.id.spinnerGender);
        spinnerRole = view.findViewById(R.id.spinnerRole);
        buttonSave = view.findViewById(R.id.buttonSave);
        buttonDelete = view.findViewById(R.id.buttonDelete);
        TextView backBtn = view.findViewById(R.id.btnReturn);
        backBtn.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());


        storeDetailViewModel.getStaff(staffId);
        storeDetailViewModel.getGetStaffResponse().observe(getViewLifecycleOwner(), staffResource -> {
            switch (staffResource.getStatus()) {
                case SUCCESS:
                    Log.d("FragmentStaffDetail","SUCCESS RETRIVE UPDATE RUN");
                    User staff = staffResource.getData();
                    if (staff != null) {
                        editTextName.setText(staff.getName());
                        editTextEmail.setText(staff.getEmail());
                        editTextPhone.setText(staff.getPhonenumber());

                        // Gender spinner
                        String[] genders = getResources().getStringArray(R.array.gender_array);
                        for (int i = 0; i < genders.length; i++) {
                            if (genders[i].equalsIgnoreCase(staff.getGender())) {
                                spinnerGender.setSelection(i);
                                break;
                            }
                        }

                        // Role spinner
                        String[] roles = getResources().getStringArray(R.array.role_array);
                        for (int i = 0; i < roles.length; i++) {
                            if (roles[i].equalsIgnoreCase(staff.getTopRole())) {
                                spinnerRole.setSelection(i);
                                break;
                            }
                        }
                    }
                    break;

                case ERROR:
                    Toast.makeText(getContext(), "Lỗi khi tải dữ liệu nhân viên: " + staffResource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;

                case LOADING:
                    // Show loading spinner or disable input if needed
                    break;
            }
        });

        // Button save and delete logic stays unchanged
        buttonSave.setOnClickListener(v -> {
            User updatedStaff = new User();
            updatedStaff.setId(staffId);
            updatedStaff.setName(editTextName.getText().toString());
            updatedStaff.setEmail(editTextEmail.getText().toString());
            updatedStaff.setPhonenumber(editTextPhone.getText().toString());
            String eng_gender = spinnerGender.getSelectedItem().toString().equals("Nam") ? "male" : "female";
            updatedStaff.setGender(eng_gender);
            String eng_role = spinnerRole.getSelectedItem().toString().equals("Quản lý") ? "manager" : "staff";
            updatedStaff.setRole(new ArrayList<>(Arrays.asList(eng_role)));
            storeDetailViewModel.updateStaff(staffId, updatedStaff);
            Toast.makeText(getContext(), "Đã cập nhật nhân viên", Toast.LENGTH_SHORT).show();
        });

        buttonDelete.setOnClickListener(v -> {
            storeDetailViewModel.deleteStaff(staffId);
            Toast.makeText(getContext(), "Đã xóa nhân viên", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        });
        return view;
    }


    public void returnBack(View view) {
//        requireActivity().getSupportFragmentManager().popBackStack();
        requireActivity().onBackPressed();
    }
}