package com.example.food_ordering_mobile_app.adapters.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.topping.ToppingOrderItemAdapter;
import com.example.food_ordering_mobile_app.models.order.OrderItem;
import com.example.food_ordering_mobile_app.models.topping.Topping;
import com.example.food_ordering_mobile_app.ui.orders.FragmentOrderItemDetailDialog;

import java.util.ArrayList;
import java.util.List;

public class ModifyOrderAdapter extends RecyclerView.Adapter<ModifyOrderAdapter.OrderItemViewHolder> {
    private ArrayList<OrderItem> orderItems;

    public ModifyOrderAdapter(ArrayList<OrderItem> items) {
        this.orderItems = items;
    }


    public void setOrderItems(ArrayList<OrderItem> items) {
        this.orderItems = items;
        notifyDataSetChanged();
    }

    public void addNewDish(OrderItem newItem) {
        newItem.setQuantity(1);
        orderItems.add(newItem);
        notifyItemInserted(orderItems.size() - 1);
    }

    public ArrayList<OrderItem> getModifiedItems() {
        return orderItems;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modify_order, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem item = orderItems.get(position);
        holder.bind(item);
    }


    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        EditText etQuantity;
        ImageButton btnIncrease, btnDecrease, btnRemove;
        RecyclerView recyclerToppings;
        ToppingOrderItemAdapter toppingOrderItemAdapter;



        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_dish_name);
            etQuantity = itemView.findViewById(R.id.et_quantity);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            btnRemove = itemView.findViewById(R.id.btn_remove);
            recyclerToppings = itemView.findViewById(R.id.recycler_toppings);
        }

        public void bind(OrderItem item) {
            tvName.setText(item.getDish().getName());
            etQuantity.setText(String.valueOf(item.getQuantity()));

            // Recreate toppings adapter with updated toppings
            List<Topping> updatedToppings = item.getToppings() != null ? item.getToppings() : new ArrayList<>();
            toppingOrderItemAdapter = new ToppingOrderItemAdapter(new ArrayList<>(updatedToppings));
            recyclerToppings.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            recyclerToppings.setAdapter(toppingOrderItemAdapter);

            // Dish quantity logic
            btnIncrease.setOnClickListener(v -> {
                int qty = item.getQuantity() + 1;
                item.setQuantity(qty);
                etQuantity.setText(String.valueOf(qty));
            });

            btnDecrease.setOnClickListener(v -> {
                int qty = item.getQuantity();
                if (qty > 1) {
                    qty--;
                    item.setQuantity(qty);
                    etQuantity.setText(String.valueOf(qty));
                }
            });

            itemView.setOnClickListener(v -> {
                androidx.fragment.app.FragmentManager fragmentManager =
                        ((androidx.fragment.app.FragmentActivity) v.getContext()).getSupportFragmentManager();

                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;

                FragmentOrderItemDetailDialog dialog = FragmentOrderItemDetailDialog.newInstance(item, position);
                dialog.setOnOrderItemUpdatedListener((updatedItem, pos) -> {
                    orderItems.set(pos, updatedItem);
                    notifyItemChanged(pos); // triggers bind again
                }, position);

                dialog.show(fragmentManager, "OrderItemDetailDialog");
            });

            btnRemove.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    orderItems.remove(pos);
                    notifyItemRemoved(pos);
                }
            });


        }


    }


}
