package com.example.food_ordering_mobile_app.adapters.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.ui.orders.FragmentOrderDetail;
import com.example.food_ordering_mobile_app.utils.Function;

import java.util.List;

public class LatestOrderAdapter extends RecyclerView.Adapter<LatestOrderAdapter.ViewHolder> {

    private Context context;
    private List<Order> ordersList;
    private OnOrderClickListener onOrderClickListener;

    // Interface for handling item clicks
    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    // Constructor without click listener
    public LatestOrderAdapter(Context context, List<Order> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    // Constructor with click listener
    public LatestOrderAdapter(Context context, List<Order> ordersList, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.ordersList = ordersList;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ensure you are inflating the correct layout for orders
        View view = LayoutInflater.from(context).inflate(R.layout.item_latest_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = ordersList.get(position);

        // Bind order details to the UI
        holder.orderNumber.setText(String.valueOf(Function.generateOrderNumber(order.getId())));
        holder.customerName.setText(order.getCustomerName());
        holder.orderSummary.setText(order.getOrderSummary());
        holder.orderItems.setText(order.getOrderDetail());
        holder.pickupTime.setText("Không xác định");
        holder.orderTime.setText("Không xác định");

        // Handle click event
        holder.itemView.setOnClickListener(v -> {
            if (onOrderClickListener != null) {
                onOrderClickListener.onOrderClick(order);
            }
        });

        holder.btnDetail.setOnClickListener(v -> {
            if (onOrderClickListener != null) {
                onOrderClickListener.onOrderClick(order);
            }

            // Create a new instance of the fragment with arguments
            FragmentOrderDetail fragment = FragmentOrderDetail.newInstance(order.getId(), "2");

            // Get FragmentManager (make sure this code runs inside an Activity or Fragment)
            FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();

            // Begin transaction and replace the container
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_store_main, fragment)  // Make sure R.id.fragment_container exists in your layout
                    .addToBackStack(null)  // Adds to back stack so user can go back
                    .commit();
        });

        holder.btnConfirm.setOnClickListener(v -> {
            // Implement order confirmation logic here (if needed)
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size(); // Return the correct size of the list
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumber, customerName, orderSummary, orderItems, pickupTime, orderTime;
        Button btnDetail, btnConfirm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNumber = itemView.findViewById(R.id.order_number);
            customerName = itemView.findViewById(R.id.customer_name);
            orderSummary = itemView.findViewById(R.id.order_summary);
            orderItems = itemView.findViewById(R.id.order_items);
            pickupTime = itemView.findViewById(R.id.pickup_time);
            orderTime = itemView.findViewById(R.id.order_time);
            btnDetail = itemView.findViewById(R.id.btn_view_more);
            btnConfirm = itemView.findViewById(R.id.btn_confirm);
        }
    }
}
