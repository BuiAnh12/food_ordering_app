package com.example.food_ordering_mobile_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.ui.orders.FragmentOrderDetail;

import java.util.List;

public class OrderConfirmedAdapter extends RecyclerView.Adapter<OrderConfirmedAdapter.ViewHolder> {

    private Context context;
    private List<Order> ordersList;
    private OrderConfirmedAdapter.OnOrderClickListener onOrderClickListener;


    // Interface for handling item clicks
    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    // Constructor without click listener
    public OrderConfirmedAdapter(Context context, List<Order> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    // Constructor with click listener
    public OrderConfirmedAdapter(Context context, List<Order> ordersList, OrderConfirmedAdapter.OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.ordersList = ordersList;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @Override
    public OrderConfirmedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ensure you are inflating the correct layout for orders
        View view = LayoutInflater.from(context).inflate(R.layout.item_confirmed_order, parent, false);
        return new OrderConfirmedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderConfirmedAdapter.ViewHolder holder, int position) {
        Order order = ordersList.get(position);
        holder.orderId.setText(!TextUtils.isEmpty(String.valueOf(order.getId())) ? "#" + order.getId() : "#0000");
        holder.pickupTime.setText("Default picup time");
        holder.customerName.setText(!TextUtils.isEmpty(order.getCustomerName()) ? order.getUser().getName() : "Unknown");
        holder.status.setText(!TextUtils.isEmpty(order.getStatus()) ? order.getStatus() : "Pending");



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of the fragment with arguments
                FragmentOrderDetail fragment = FragmentOrderDetail.newInstance("1", "3");

                // Get FragmentManager (make sure this code runs inside an Activity or Fragment)
                FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();

                // Begin transaction and replace the container
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_store_main, fragment)  // Make sure R.id.fragment_container exists in your layout
                        .addToBackStack(null)  // Adds to back stack so user can go back
                        .commit();
            }
        });

        holder.btnInformDriver.setOnClickListener(v -> {
            // Implement order confirmation logic here (if needed)
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size(); // Return the correct size of the list
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumber, orderId, pickupTime, customerName,status, itemsNumber;
        Button btnInformDriver;

        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNumber = itemView.findViewById(R.id.order_number);
            customerName = itemView.findViewById(R.id.customer_name);
            pickupTime = itemView.findViewById(R.id.pickup_time);
            btnInformDriver = itemView.findViewById(R.id.btn_confirm);
            orderId =  itemView.findViewById(R.id.order_id);
            status = itemView.findViewById(R.id.status);
            itemsNumber = itemView.findViewById(R.id.items_number);
            cardView = itemView.findViewById(R.id.item_confirmed_order);

        }
    }
}
