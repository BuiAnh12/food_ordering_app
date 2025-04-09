package com.example.food_ordering_mobile_app.ui.orders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.dish.DishToOrderAdapter;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.DishViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FragmentDishSelectionDialog extends DialogFragment {

    private RecyclerView dishRecyclerView;
    private DishToOrderAdapter dishToOrderAdapter;
    private DishViewModel dishViewModel;

    public interface OnDishSelectedListener {
        void onDishSelected(Dish dish);
    }

    private OnDishSelectedListener listener;

    public FragmentDishSelectionDialog(OnDishSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            int margin = (int) getResources().getDisplayMetrics().density * 24; // 24dp margin
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int dialogWidth = screenWidth - (margin * 2);

            getDialog().getWindow().setLayout(
                    dialogWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dish_selection_dialog, container, false);
        dishRecyclerView = view.findViewById(R.id.recycler_dish_list);
        dishRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dishToOrderAdapter = new DishToOrderAdapter(new ArrayList<>(), dish -> {
            listener.onDishSelected(dish);
            Log.e("FragmentDishSelectionDialog", "Dish selected: " + dish.getName());
            dismiss(); // Close dialog after selection
        });
        Button closeButton = view.findViewById(R.id.btn_close);
        closeButton.setOnClickListener(v -> dismiss());
        dishRecyclerView.setAdapter(dishToOrderAdapter);

        dishViewModel = new ViewModelProvider(this).get(DishViewModel.class);
        dishViewModel.getAllDishes("",100,1);
        dishViewModel.getAllDishesResponse().observe(getViewLifecycleOwner(), new Observer<Resource<List<Dish>>>() {
            @Override
            public void onChanged(Resource<List<Dish>> listResource) {
                switch (listResource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (listResource.getData() != null) {
                            dishToOrderAdapter.setDishes(listResource.getData());
                            Log.e("FragmentDishSelectionDialog", "Data: " + Arrays.toString(listResource.getData().toArray()));
                            dishToOrderAdapter.notifyDataSetChanged(); // Thông báo Adapter cập nhật
                        }
                        break;
                    case ERROR:
                        Log.e("FragmentLatestOrder", "Error: " + listResource.getMessage());
                        Toast.makeText(getContext(), listResource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        return view;
    }
}