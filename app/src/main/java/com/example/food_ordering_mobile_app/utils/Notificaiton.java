package com.example.food_ordering_mobile_app.utils;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.ui.MainStoreActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Notificaiton {
    private static final String CHANNEL_ID = "default_channel";
    private static final String CHANNEL_NAME = "Default Channel";
    private static final String CHANNEL_DESCRIPTION = "General notifications";
    private static final int NOTIFICATION_ID = 1001;
    public static final String FRAGMENT_KEY = "target_fragment";

    private final Context context;

    public Notificaiton(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Request notification permission on Android 13+.
     */
    public void requestNotificationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasNotificationPermission()) {
                if (activity.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    // Optionally show rationale here before requesting permission
                }
                activity.requestPermissions(
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        101
                );
            }
        }
    }

    /**
     * Check if notification permission is granted (Android 13+).
     */
    public boolean hasNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        return true; // Permission not required below Android 13
    }


    /**
     * Send notification with separate IDs to avoid interference.
     *
     * @param title          Notification title
     * @param message        Notification message
     * @param targetFragment The fragment class name to open inside the target activity (nullable)
     * @param menuItemId     The ID of the bottom navigation item to select
     */
    public void sendNotificationSwitchMenu(String title, String message, String targetFragment, int menuItemId) {
        Class<?> targetActivity = MainStoreActivity.class;
        if (!hasNotificationPermission()) {
            // Permission not granted, don't send notification
            return;
        }

        Intent intent = new Intent(context, targetActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Pass both the target fragment and the selected menu item ID
        if (targetFragment != null) {
            intent.putExtra(FRAGMENT_KEY, targetFragment);
        }
        intent.putExtra("menu_item_id", menuItemId);

        // Use unique request code for each notification to keep them separate
        int requestCode = (int) (System.currentTimeMillis() & 0xfffffff);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                requestCode,  // Unique request code
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // Use the same unique ID for the notification itself
        notificationManager.notify(requestCode, builder.build());
    }


}
