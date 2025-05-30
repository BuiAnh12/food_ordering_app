package com.example.food_ordering_mobile_app.ui.staffs;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.staffs.StaffAdapter;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.ui.common.CustomHeaderView;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.StoreDetailViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentStaffs extends Fragment implements SwipeRefreshLayout.OnRefreshListener{


    private StaffAdapter staffAdapter;

    private StoreDetailViewModel storeDetailViewModel;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;
    private List<User> staffList = new ArrayList<>();

    public FragmentStaffs() {
        // Required empty public constructor
    }

    public static FragmentStaffs newInstance() {
        FragmentStaffs fragment = new FragmentStaffs();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staffs, container, false);
        storeDetailViewModel = new ViewModelProvider(this).get(StoreDetailViewModel.class);
        staffAdapter = new StaffAdapter(staffList, staff -> {
            // Create the detail fragment with the staff ID as an argument
            FragmentStaffDetail fragment = new FragmentStaffDetail();
            Bundle args = new Bundle();
            args.putString("STAFF_ID", staff.getId());
            fragment.setArguments(args);

            // Replace the current fragment with FragmentStaffDetail
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.staff_fragment_layout, fragment) // Make sure fragment_container is the ID of your FrameLayout or NavHostFragment
                    .addToBackStack(null)
                    .commit();
        });
        Button addStaffButton = view.findViewById(R.id.button_add_staff);
        recyclerView = view.findViewById(R.id.staff_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(staffAdapter);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        EditText searchInput = view.findViewById(R.id.search_input);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                staffAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });
        getStaffList();
        addStaffButton.setOnClickListener(this::addStaff);
        // Inflate the layout for this fragment
        return view;
    }

    public void addStaff(View view) {
        FragmentAddStaffs fragmentAddStaffs = new FragmentAddStaffs();
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.staff_fragment_layout, fragmentAddStaffs)
                .addToBackStack(null)
                .commit();
    }



    public void getStaffList() {
        if (storeDetailViewModel == null) {
            Log.e("FragmentLatestOrder", "OrderViewModel is null. Skipping filter.");
            return;
        }
        staffList.clear();
        storeDetailViewModel.getStaffList();
        // Quan sát dữ liệu
        storeDetailViewModel.getGetStaffListResponse().observe(getViewLifecycleOwner(), new Observer<Resource<List<User>>>() {
            @Override
            public void onChanged(Resource<List<User>> listResource) {
                switch (listResource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (listResource.getData() != null) {
                            Log.e("FragmentLatestOrder", "Data: " + Arrays.toString(listResource.getData().toArray()));
                            staffAdapter.updateData(listResource.getData());
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        break;
                    case ERROR:
                        Log.e("FragmentLatestOrder", "Error: " + listResource.getMessage());
                        Toast.makeText(getContext(), listResource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        getStaffList();
    }
}