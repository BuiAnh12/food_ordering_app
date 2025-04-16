package com.example.food_ordering_mobile_app.ui.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.topping.ToppingAdapter;
import com.example.food_ordering_mobile_app.models.topping.ToppingGroup;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.ToppingViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;

public class FragmentTopping extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private RecyclerView recyclerView;
    private ToppingAdapter adapter;
    private ToppingViewModel toppingViewModel;

    private List<ToppingGroup> toppingGroups = new ArrayList<>();


    public FragmentTopping() {
        // Required empty public constructor
    }

    public static FragmentTopping newInstance() {
        return new FragmentTopping();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topping, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Button addGroupBtn = view.findViewById(R.id.btn_add_group);
        addGroupBtn.setOnClickListener(this::onAddToppingGroup);
        adapter = new ToppingAdapter(toppingGroups);
        recyclerView.setAdapter(adapter);

        toppingViewModel = new ViewModelProvider(this).get(ToppingViewModel.class);
        toppingViewModel.getAllToppings(10, 1);

        toppingViewModel.getAlllToppingsResponse().observe(getViewLifecycleOwner(), new Observer<Resource<List<ToppingGroup>>>() {
            @Override
            public void onChanged(Resource<List<ToppingGroup>> listResource) {
                switch (listResource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (listResource.getData() != null) {
                            toppingGroups.clear();
                            Log.d("FragmentToppingFragment", "Data: " + Arrays.toString(listResource.getData().toArray()));
                            toppingGroups.addAll(listResource.getData());
                            adapter.notifyDataSetChanged();
                        }
                        break;
                    case ERROR:
                        Log.e("FragmentToppingFragment", "Error: " + listResource.getMessage());
                        Toast.makeText(getContext(), listResource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        toppingViewModel.addToppingToGroupResponse().observe(getViewLifecycleOwner(), new Observer<Resource<Void>>() {
            @Override
            public void onChanged(Resource<Void> toppingGroupResource) {
                switch (toppingGroupResource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        Toast.makeText(getContext(), "Thêm nhóm thành công", Toast.LENGTH_SHORT).show();
                        Log.d("FragmentToppingFragment", "Thêm thành công" );
                        adapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        Toast.makeText(getContext(), "Thêm nhóm có lỗi", Toast.LENGTH_SHORT).show();
                        break;
                        }
            }
        });
        return view;
    }

    private void onAddToppingGroup(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thêm nhóm topping");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Tên nhóm");
        builder.setView(input);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String groupName = input.getText().toString().trim();
            if (!groupName.isEmpty()) {
                ToppingGroup newGroup = new ToppingGroup();
                newGroup.setName(groupName);
                newGroup.setToppings(new ArrayList<>()); // start with empty toppings
                toppingGroups.add(newGroup);
                toppingViewModel.addToppingGroup(newGroup);
            } else {
                Toast.makeText(getContext(), "Tên nhóm không được để trống", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}