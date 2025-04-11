package com.example.food_ordering_mobile_app.adapters.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.ui.orders.FragmentOrderDetail;
import com.example.food_ordering_mobile_app.ui.orders.FragmentOrderHistoryDetail;
import com.example.food_ordering_mobile_app.utils.Function;

import java.util.List;

public class InnerHistoryAdapter extends RecyclerView.Adapter<InnerHistoryAdapter.ViewHolder> {
    private List<Order> orderList;

    public InnerHistoryAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public InnerHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHistoryAdapter.ViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.customerName.setText(order.getCustomerName() != null ? order.getCustomerName() : "Khách hàng không xác định");
        holder.orderNumber.setText(Function.generateOrderNumber(order.getId()) != "" ? Function.generateOrderNumber(order.getId()) : "N/A");
        holder.pickupTime.setText("Không có thời gian");
        holder.totalItems.setText(order.getTotalItems() > 0 ? String.valueOf(order.getTotalItems()) : "0");
        holder.totalPrice.setText(order.getTotalPrice() > 0 ? String.valueOf(order.getTotalPrice()) + "₫" : "0₫");
        holder.distance.setText("Không xác định");
        holder.status.setText(order.getStatus() != null ? order.getStatus() : "Không xác định");

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of the fragment with arguments
                FragmentOrderDetail fragment = FragmentOrderDetail.newInstance(order.getId(), "3");

                // Get FragmentManager (make sure this code runs inside an Activity or Fragment)
                FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();

                // Begin transaction and replace the container
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_store_main, fragment)  // Make sure R.id.fragment_container exists in your layout
                        .addToBackStack(null)  // Adds to back stack so user can go back
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, orderNumber, pickupTime, totalItems, totalPrice, distance, status;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customer_name);
            orderNumber = itemView.findViewById(R.id.order_number);
            pickupTime = itemView.findViewById(R.id.pickup_time);
            totalItems = itemView.findViewById(R.id.item_count);
            totalPrice = itemView.findViewById(R.id.total_price);
            distance = itemView.findViewById(R.id.distance_value);
            status = itemView.findViewById(R.id.history_status);
            cardView = itemView.findViewById(R.id.item_preorder);
        }
    }
}
