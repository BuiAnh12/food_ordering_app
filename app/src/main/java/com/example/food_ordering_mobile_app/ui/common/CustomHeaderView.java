package com.example.food_ordering_mobile_app.ui.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageButton;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.notification.Notification;
import com.example.food_ordering_mobile_app.network.SocketManager;
import com.example.food_ordering_mobile_app.ui.notifications.NotificationActivity;
import com.example.food_ordering_mobile_app.viewModel.NotificationViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomHeaderView extends LinearLayout {
    private TextView tvTitle;
    private ImageButton btnNotification, btnUser;
    private TextView unreadNotificationCount;
    private View unreadNotificationBadge;

    private NotificationViewModel notificationViewModel;
    private LifecycleOwner lifecycleOwner;
    private boolean isNotificationObserverSet = false;

    // Controlled from outside
    private List<Notification> notifications = new ArrayList<>();

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        setUpUserNotification();
    }

    public CustomHeaderView(Context context) {
        super(context);
        init(context);
    }

    public CustomHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_custom_header_view, this, true);
        setBackgroundResource(R.color.whiteColor);
        setElevation(16f);

        tvTitle = findViewById(R.id.textView);
        btnNotification = findViewById(R.id.goToNotificationBtn);
        btnUser = findViewById(R.id.gotToProfile);
        unreadNotificationCount = findViewById(R.id.unreadNotificationCount);
        unreadNotificationBadge = unreadNotificationCount;

        notificationViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(NotificationViewModel.class);

        btnNotification.setOnClickListener(v -> {
            context.startActivity(new Intent(context, NotificationActivity.class));
            markAllAsRead(); // Mark all as read on click
        });

        btnUser.setOnClickListener(v -> {
            context.startActivity(new Intent(context, com.example.food_ordering_mobile_app.ui.profile.ProfileActivity.class));
        });
    }

    private void setUpUserNotification() {
        if (isNotificationObserverSet) return;

        SocketManager.connectSocket(getContext(), notificationViewModel);

        notificationViewModel.getNewNotification().observe(lifecycleOwner, newNotifications -> {
            if (newNotifications != null) {
                notifications = newNotifications;
                updateUnreadNotificationCount();
            } else {
                notifications = new ArrayList<>();
                updateUnreadNotificationCount();
            }
        });

        isNotificationObserverSet = true;
    }

    public void setNotifications(List<Notification> newNotifications) {
        this.notifications = newNotifications != null ? newNotifications : new ArrayList<>();
        updateUnreadNotificationCount();
    }

    public void clearNotifications() {
        this.notifications.clear();
        updateUnreadNotificationCount();
    }

    public void markAllAsRead() {
        for (Notification notification : notifications) {
            notification.setStatus("read");
        }
        updateUnreadNotificationCount();
    }

    private void updateUnreadNotificationCount() {
        int unreadCount = 0;
        Set<String> seenIds = new HashSet<>();

        for (Notification notification : notifications) {
            if ("unread".equals(notification.getStatus()) && !seenIds.contains(notification.getId())) {
                unreadCount++;
                seenIds.add(notification.getId());
            }
        }

        if (unreadCount > 0) {
            unreadNotificationBadge.setVisibility(VISIBLE);
            unreadNotificationCount.setText(String.valueOf(unreadCount));
            unreadNotificationCount.setTextColor(Color.WHITE);
        } else {
            unreadNotificationBadge.setVisibility(GONE);
        }
    }

    public void setText(String text) {
        tvTitle.setText(text);
    }
}
