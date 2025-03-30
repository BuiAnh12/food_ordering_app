package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.order.OrderDetail;

import java.util.List;

public class OrderItemDetailAdapter extends RecyclerView.Adapter<OrderItemDetailAdapter.ViewHolder> {
    private Context context;

    private List<OrderDetail> orderList;

    public OrderItemDetailAdapter(Context context, List<OrderDetail> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_order_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail order = orderList.get(position);

        holder.itemName.setText(order.getDishName());
        holder.quantity.setText(String.valueOf(order.getQuantity()));
        holder.unitPrice.setText(String.valueOf(order.getUnitPrice()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, quantity, unitPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            quantity = itemView.findViewById(R.id.quantity);
            unitPrice = itemView.findViewById(R.id.unit_price);
        }
    }
}
