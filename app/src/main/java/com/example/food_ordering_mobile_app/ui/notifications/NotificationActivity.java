package com.example.food_ordering_mobile_app.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.notification.NotificationAdapter;
import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.notification.Notification;
import com.example.food_ordering_mobile_app.network.SocketManager;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.NotificationViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private NotificationViewModel notificationViewModel;
    private RecyclerView notificationRecyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        notificationRecyclerView = findViewById(R.id.notificationRecyclerView);

        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        SocketManager.connectSocket(this, notificationViewModel);  // Connect to socket
        setupStoreNotification();  // Setup initial notification

        // Pull the latest notifications from the server
        notificationViewModel.getStoreNotification(20,1);
        notificationViewModel.getStoreNotificationResponse().observe(this, new Observer<Resource<ApiResponse<List<Notification>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Notification>>> resource) {
                if (resource.getData() == null || resource.getStatus() != Resource.Status.SUCCESS) {
                    Log.e("NotificationActivity", "Error fetching notifications:");
                    Log.e("NotificationActivity", "Error: " + resource.getMessage());
                }
                else {
                    notificationList.clear();
                    notificationList.addAll(resource.getData().getData());  // Update the list
                    notificationAdapter.notifyDataSetChanged();
                }

            }
        });


        // Observe notification list changes from ViewModel
        notificationViewModel.getNotifications().observe(this, new Observer<List<Notification>>() {
            @Override
            public void onChanged(List<Notification> notifications) {
                notificationList.addAll(notifications);  // Update the list
                notificationAdapter.notifyDataSetChanged();  // Notify adapter of changes
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // You can reload the data here if needed
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setupStoreNotification() {
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(NotificationActivity.this, this.notificationList);
        notificationRecyclerView.setAdapter(notificationAdapter);

        notificationViewModel.getStoreNotificationResponse().observe(this, new Observer<Resource<ApiResponse<List<Notification>>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<List<Notification>>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        notificationList.clear();
                        notificationList.addAll(resource.getData().getData());
                        notificationAdapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });

//        notificationViewModel.getNotificationsResponse().observe(this, new Observer<Resource<List<Notification>>>() {
//            @Override
//            public void onChanged(Resource<List<Notification>> resource) {
//                    switch (resource.getStatus()) {
//                        case LOADING:
//                            swipeRefreshLayout.setRefreshing(true);
//                            break;
//                        case SUCCESS:
//                            swipeRefreshLayout.setRefreshing(false);
//                            notificationList.clear();
//                            notificationList.addAll(resource.getData());
//                            notificationAdapter.notifyDataSetChanged();
//                            break;
//                        case ERROR:
//                            swipeRefreshLayout.setRefreshing(false);
//                            break;
//                    }
//            }
//        });
    }

    public void goBack(View view) {
        finish();
    }
}
