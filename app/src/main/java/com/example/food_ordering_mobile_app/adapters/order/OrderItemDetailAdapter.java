package com.example.food_ordering_mobile_app.adapters.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.order.OrderDisplayItem;
import com.example.food_ordering_mobile_app.models.order.OrderItem;
import com.example.food_ordering_mobile_app.models.topping.Topping;
import com.example.food_ordering_mobile_app.utils.Function;

import java.util.ArrayList;
import java.util.List;

public class OrderItemDetailAdapter extends RecyclerView.Adapter<OrderItemDetailAdapter.ViewHolder> {
    private final Context context;
    private final List<OrderDisplayItem> displayItems;

    public OrderItemDetailAdapter(Context context, List<OrderItem> orderItems) {
        this.context = context;
        this.displayItems = flattenOrderItems(orderItems);
    }

    private List<OrderDisplayItem> flattenOrderItems(List<OrderItem> orderList) {
        List<OrderDisplayItem> result = new ArrayList<>();

        for (OrderItem order : orderList) {
            result.add(new OrderDisplayItem(order.getDish(), order.getQuantity()));

            if (order.getToppings() != null) {
                for (Topping topping : order.getToppings()) {
                    result.add(new OrderDisplayItem(topping)); // toppings don’t need quantity
                }
            }
        }

        return result;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_order_summary, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDisplayItem item = displayItems.get(position);

        if (item.isTopping()) {
            holder.quantityLayout.setVisibility(View.GONE);
            holder.name.setText("- " + item.getTopping().getName());
            holder.name.setTextSize(16f);
            // Indent topping by applying left padding
            int indentPx = (int) (20 * context.getResources().getDisplayMetrics().density); // 16dp
            holder.name.setPadding(indentPx, 0, 0, 0);
            holder.price.setText(Function.priceConverter(item.getTopping().getPrice()));
        } else {
            holder.quantityLayout.setVisibility(View.VISIBLE);
            holder.quantity.setText(String.valueOf(item.getQuantity()));
            holder.name.setText(item.getDish().getName());
            holder.name.setTextSize(18f);
            holder.name.setPadding(0, 0, 0, 0); // remove indent
            holder.price.setText(Function.priceConverter(item.getDish().getPrice()));
        }

    }

    @Override
    public int getItemCount() {
        return displayItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout quantityLayout;
        TextView quantity, name, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quantityLayout = itemView.findViewById(R.id.quantity_layout);
            quantity = itemView.findViewById(R.id.quantity);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
        }
    }
}
