package com.example.food_ordering_mobile_app.ui.orders;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.topping.AvailableAndSelectedToppingAdapter;
import com.example.food_ordering_mobile_app.adapters.topping.ToppingOrderItemAdapter;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.DishCategory;
import com.example.food_ordering_mobile_app.models.order.OrderItem;
import com.example.food_ordering_mobile_app.models.topping.Topping;
import com.example.food_ordering_mobile_app.models.topping.ToppingGroup;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.DishViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentOrderItemDetailDialog extends DialogFragment  {

    private static final String ARG_ORDER_ITEM = "order_item";
    private static final String ARG_POSITION = "position";
    private OrderItem orderItem;

    private DishViewModel dishViewModel;

    private OnOrderItemUpdatedListener listener;
    private int itemPosition;
    public interface OnOrderItemUpdatedListener {
        void onOrderItemUpdated(OrderItem updatedItem, int position);
    }

    public void setOnOrderItemUpdatedListener(OnOrderItemUpdatedListener listener, int position) {
        this.listener = listener;
        this.itemPosition = position;
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

    public static FragmentOrderItemDetailDialog newInstance(OrderItem item, int position) {
        FragmentOrderItemDetailDialog fragment = new FragmentOrderItemDetailDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORDER_ITEM, item);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        View view = inflater.inflate(R.layout.fragment_order_item_detail_dialog, container, false);

        if (getArguments() != null) {
            orderItem = (OrderItem) getArguments().getSerializable(ARG_ORDER_ITEM);
            itemPosition = getArguments().getInt(ARG_POSITION);
        }

        TextView tvName = view.findViewById(R.id.tv_dish_name);
        TextView tvPrice = view.findViewById(R.id.tv_dish_price);
        EditText etQuantity = view.findViewById(R.id.et_quantity);
        ImageButton btnInc = view.findViewById(R.id.btn_increase);
        ImageButton btnDec = view.findViewById(R.id.btn_decrease);
        RecyclerView toppingList = view.findViewById(R.id.recycler_toppings);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnConfirm = view.findViewById(R.id.btn_confirm);

        tvName.setText(orderItem.getDish().getName());
        tvPrice.setText(orderItem.getDish().getFortmatedPrice());
        etQuantity.setText(String.valueOf(orderItem.getQuantity()));

        // Toppings adapter
        AvailableAndSelectedToppingAdapter adapter = new AvailableAndSelectedToppingAdapter(orderItem.getToppings(), new ArrayList<>());
        toppingList.setLayoutManager(new LinearLayoutManager(requireContext()));
        toppingList.setAdapter(adapter);

        dishViewModel = new ViewModelProvider(this).get(DishViewModel.class);
        dishViewModel.getToppingsFromDish(orderItem.getDish().getId());
        dishViewModel.getToppingsResponse().observe(getViewLifecycleOwner(), new Observer<Resource<List<ToppingGroup>>>() {
            @Override
            public void onChanged(Resource<List<ToppingGroup>> listResource) {
                switch (listResource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (listResource.getData() != null) {
                            Log.d("FragmentHistoryOrder", "Data: " + Arrays.toString(listResource.getData().toArray()));
                            List<ToppingGroup> toppingGroups = listResource.getData();
                            List<Topping> toppings = new ArrayList<>();
                            for (ToppingGroup toppingGroup : toppingGroups) {
                                toppings.addAll(toppingGroup.getToppings());
                            }
                            adapter.setAvailableToppings(toppings);
                        }
                        break;
                    case ERROR:
                        Log.e("FragmentConfirmOrder", "Error: " + listResource.getMessage());
                        Toast.makeText(getContext(), listResource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        btnConfirm.setOnClickListener(v -> {
            orderItem.setQuantity(Integer.parseInt(etQuantity.getText().toString()));
            orderItem.setToppings((ArrayList<Topping>) adapter.getSelectedToppings());
            if (listener != null) {

                listener.onOrderItemUpdated(orderItem, itemPosition);
            }
            dismiss();
        });

        btnInc.setOnClickListener(v -> {
            int qty = Integer.parseInt(etQuantity.getText().toString());
            qty++;
            etQuantity.setText(String.valueOf(qty));
        });

        btnDec.setOnClickListener(v -> {
            int qty = Integer.parseInt(etQuantity.getText().toString());
            if (qty > 1) qty--;
            etQuantity.setText(String.valueOf(qty));
        });

        btnCancel.setOnClickListener(v -> dismiss());

        return view;
    }
}