package com.example.food_ordering_mobile_app.adapters.order;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.ui.orders.FragmentOrderDetail;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.OrderViewModel;

import java.util.List;

public class OrderConfirmedAdapter extends RecyclerView.Adapter<OrderConfirmedAdapter.ViewHolder> {

    private Context context;
    private List<Order> ordersList;

    private OrderViewModel orderViewModel;
    private LifecycleOwner lifecycleOwner;
    // Constructor without click listener
    public OrderConfirmedAdapter(Context context, List<Order> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    public OrderConfirmedAdapter(Context context, List<Order> ordersList, OrderViewModel orderViewModel, LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.ordersList = ordersList;
        this.orderViewModel = orderViewModel;
        this.lifecycleOwner = lifecycleOwner;
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
//        holder.pickupTime.setText("Default picup time");
        holder.customerName.setText(!TextUtils.isEmpty(order.getCustomerName()) ? order.getCustomerName() : order.getUser().getName());
        holder.status.setText(!TextUtils.isEmpty(order.getStatus()) ? order.getStatus() : "Pending");
        if (order.getStatus().equals("finished")) {
            holder.btnInformDriver.setText("Đã thông báo tài xế");
            holder.btnInformDriver.setEnabled(false);
            holder.btnInformDriver.setBackgroundTintList(
                    ContextCompat.getColorStateList(context, R.color.secondaryColor)
            );

        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of the fragment with arguments
                FragmentOrderDetail fragment = FragmentOrderDetail.newInstance(order.getId(), "2");

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
            order.setStatus("finished");
            orderViewModel.updateOrder(order.getId(), order);
            orderViewModel.getUpdateOrderResponse().observe(lifecycleOwner, resource -> {
                if (resource.getStatus() == Resource.Status.SUCCESS) {
                    holder.btnInformDriver.setText("Đã thông báo tài xế");
                    holder.btnInformDriver.setEnabled(false);
                    holder.btnInformDriver.setBackgroundTintList(
                            ContextCompat.getColorStateList(context, R.color.secondaryColor)
                    );
                    holder.status.setText(order.getStatus());
                    Log.d("OrderConfirmedAdapter", "Order updated successfully");
                    Toast.makeText(context, "Nhận đơn hàng thành công", Toast.LENGTH_SHORT).show();
                }
            });
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
//            pickupTime = itemView.findViewById(R.id.pickup_time);
            btnInformDriver = itemView.findViewById(R.id.btn_confirm);
            orderId =  itemView.findViewById(R.id.order_id);
            status = itemView.findViewById(R.id.status);
            itemsNumber = itemView.findViewById(R.id.items_number);
            cardView = itemView.findViewById(R.id.item_confirmed_order);

        }
    }
}
