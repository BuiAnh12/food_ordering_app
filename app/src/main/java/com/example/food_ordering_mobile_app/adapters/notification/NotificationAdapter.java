package com.example.food_ordering_mobile_app.adapters.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.notification.Notification;
import com.example.food_ordering_mobile_app.utils.Function;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context context;
    private List<Notification> notificationList;
    private OnNotificationClickListener onNotificationClickListener;
    public interface OnNotificationClickListener {
        void onNotificationClick(Notification notification);
    }

    public NotificationAdapter(Context context, List<Notification> cartList) {
        this.context = context;
        this.notificationList = cartList;
    }

    public NotificationAdapter(Context context, List<Notification> notificationList, OnNotificationClickListener onNotificationClickListener) {
        this.context = context;
        this.notificationList = notificationList;
        this.onNotificationClickListener = onNotificationClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        // Hiển thị orderId + title + message
        String orderId = notification.getOrderId() != null ? Function.generateOrderNumber(notification.getOrderId()) : "N/A";
        String displayText;
        if (notification.getOrderId() != null) {
            displayText = "#" + orderId + " - " + notification.getTitle() + "\n" + notification.getMessage();
        }
        else {
            displayText = notification.getTitle() + "\n" + notification.getMessage();
        }

        holder.noti.setText(displayText);

        // Tính thời gian tương đối
        holder.time.setText(notification.getRelativeTime());

        boolean isRead = notification.getStatus() != null;

        if (isRead) {
            holder.notificationContainer.setBackgroundColor(context.getResources().getColor(R.color.backgroundColor));
            holder.noti.setTextColor((context.getResources().getColor(R.color.onTertiary)));
            holder.time.setTextColor((context.getResources().getColor(R.color.onTertiary)));
            holder.status.setTextColor((context.getResources().getColor(R.color.onTertiary)));
        } else {
            holder.notificationContainer.setBackgroundColor(context.getResources().getColor(R.color.notificationBgColor));
            holder.noti.setTextColor((context.getResources().getColor(R.color.onPrimary)));
            holder.time.setTextColor((context.getResources().getColor(R.color.onSecondary)));
            holder.status.setTextColor((context.getResources().getColor(R.color.primaryColor)));
        }

        holder.itemView.setOnClickListener(v -> {
            if (onNotificationClickListener != null) {
                onNotificationClickListener.onNotificationClick(notification);
            }
        });
    }


    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView noti, time, status;
        LinearLayout notificationContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationContainer = itemView.findViewById(R.id.notification_container);
            noti = itemView.findViewById(R.id.noti);
            time = itemView.findViewById(R.id.time);
            status = itemView.findViewById(R.id.status);
        }
    }
}

