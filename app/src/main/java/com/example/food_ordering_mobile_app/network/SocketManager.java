package com.example.food_ordering_mobile_app.network;
import android.content.Context;
import android.util.Log;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.notification.Notification;
import com.example.food_ordering_mobile_app.ui.MainStoreActivity;
import com.example.food_ordering_mobile_app.ui.orders.FragmentStoreOrder;
import com.example.food_ordering_mobile_app.utils.Notificaiton;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
import com.example.food_ordering_mobile_app.viewModel.NotificationViewModel;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class SocketManager {
    private static Socket mSocket;
    private static SharedPreferencesHelper sharedPreferencesHelper;

    private static boolean isSocketInitialized = false;

    private static Notificaiton notificaitonUtil;

    private static Emitter.Listener messageListener;
    private static Emitter.Listener messageDeletedListener;

    public static void requestNotificationPermission(Context context) {
        if (notificaitonUtil != null) {
            notificaitonUtil.requestNotificationPermission((android.app.Activity) context);
        }
        else {
            notificaitonUtil = new Notificaiton(context);
            notificaitonUtil.requestNotificationPermission((android.app.Activity) context);
        }

    }

    public static void connectSocket(Context context, NotificationViewModel notificationViewModel) {
        try {
            if (isSocketInitialized) {
                return;
            }
            requestNotificationPermission(context);
            isSocketInitialized = true;
            sharedPreferencesHelper = new SharedPreferencesHelper(context);
            String userId = sharedPreferencesHelper.getUserId();
            List<Notification> notifications = new ArrayList<>();

            if (userId == null) {
                Log.e("SocketManager", "User ID is not available");
                return;
            }

            mSocket = IO.socket("http://10.0.2.2:5100");
            mSocket.connect();

            mSocket.on(Socket.EVENT_CONNECT, args -> {
                Log.d("SocketManager", "Kết nối thành công đến server");
                // Khi kết nối thành công, gửi userId lên server
                mSocket.emit("registerUser", userId);
                String storeId = sharedPreferencesHelper.getStoreId();
                mSocket.emit("joinStoreRoom", storeId);
                Log.d("SocketManager", "Đã đăng ký nhận thông báo tin nhắn cho cửa hàng: " + storeId);
            });

            mSocket.on(Socket.EVENT_CONNECT_ERROR, args -> {
                Log.e("SocketManager", "Lỗi kết nối socket: " + args[0]);
            });

            mSocket.on(Socket.EVENT_DISCONNECT, args -> {
                Log.e("SocketManager", "Socket bị ngắt kết nối");
            });

            Notificaiton phoneNotification = new Notificaiton(context);
//            phoneNotification.sendNotificationSwitchMenu(
//                    "New Order",
//                    "New order have been place",
//                    "com.example.food_ordering_mobile_app.ui.orders.FragmentStoreOrder",
//                    R.id.orders_item
//            );
//
//            phoneNotification.sendNotificationSwitchMenu(
//                    "New Chat",
//                    "New chat form ...",
//                    "com.example.food_ordering_mobile_app.ui.chat.FragmentChat",
//                    R.id.messages_item
//            );





            mSocket.on("newOrderNotification", args -> {
                Log.d("SocketManager", "Đã nhận thông báo đơn hàng mới");
                try {
                    if (args.length > 0 && args[0] instanceof JSONObject) {
                        JSONObject jsonObject = (JSONObject) args[0];

                        JSONObject notification = jsonObject.getJSONObject("notification");
                        String orderId = jsonObject.getString("orderId");
                        String title = jsonObject.getString("title");
                        String message = jsonObject.getString("message");
                        String type = notification.getString("type");
                        String uId = jsonObject.getString("userId");
                        String status = notification.getString("status");
                        String id = jsonObject.getString("_id");

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

                        Date createdDate = dateFormat.parse(notification.getString("createdAt"));
                        Date updatedDate = dateFormat.parse(notification.getString("updatedAt"));

                        Timestamp createdAt = new Timestamp(createdDate.getTime());
                        Timestamp updatedAt = new Timestamp(updatedDate.getTime());

                        Notification newOrderNotification = new Notification(id, uId, title, message, type, status,  orderId, createdAt, updatedAt);

                        phoneNotification.sendNotificationSwitchMenu(
                                "New Order",
                                "New order have been place",
                                "com.example.food_ordering_mobile_app.ui.orders.FragmentStoreOrder",
                                R.id.orders_item
                        );


                        // Gửi thông báo mới đến ViewModel
                        notificationViewModel.addNewNotification(newOrderNotification);

                        Log.d("SocketManager", "Đã nhận thông báo đơn hàng mới: " + newOrderNotification.toString());
                    }
                } catch (Exception e) {
                    Log.e("SocketManager", "Lỗi khi nhận thông báo đơn hàng mới: ", e);
                }
            });

            mSocket.on("getStoreNotifications", args -> {
                Log.d("SocketManager", "Lấy danh sách thông báo cửa hàng");
                try {
                    if (args.length > 0 && args[0] instanceof JSONArray) {
                        JSONArray jsonArray = (JSONArray) args[0];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String id = jsonObject.getString("_id");
                            String uId = jsonObject.getString("userId");
                            String title = jsonObject.getString("title");
                            String message = jsonObject.getString("message");
                            String type = jsonObject.getString("type");
                            String status = jsonObject.getString("status");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

                            // Chuyển đổi từ chuỗi JSON sang Date
                            Date createdDate = dateFormat.parse(jsonObject.getString("createdAt"));
                            Date updatedDate = dateFormat.parse(jsonObject.getString("updatedAt"));

                            // Chuyển Date thành Timestamp
                            Timestamp createdAt = new Timestamp(createdDate.getTime());
                            Timestamp updatedAt = new Timestamp(updatedDate.getTime());

                            // Tạo đối tượng Notification
                            Notification notification = new Notification(id, uId, title, message, type, status, createdAt, updatedAt);
                            notifications.add(notification);
                        }

                        Log.d("SocketManager", "Danh sách thông báo: " + notifications.toString());

                        // Cập nhật UI hoặc ViewModel nếu cần
                        notificationViewModel.updateNotifications(notifications);
                    } else {
                        Log.e("SocketManager", "Không có dữ liệu hợp lệ từ server!");
                    }
                } catch (Exception e) {
                    Log.e("SocketManager", "Lỗi khi xử lý thông báo: ", e);
                }
            });

            // 🟢 Lắng nghe thông báo mới từ server
            mSocket.on("newNotification", args -> {
                try {
                    if (args.length > 0 && args[0] instanceof JSONObject) {
                        JSONObject jsonObject = (JSONObject) args[0];

                        String id = jsonObject.getString("_id");
                        String uId = jsonObject.getString("userId");
                        String title = jsonObject.getString("title");
                        String message = jsonObject.getString("message");
                        String type = jsonObject.getString("type");
                        String status = jsonObject.getString("status");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

                        Date createdDate = dateFormat.parse(jsonObject.getString("createdAt"));
                        Date updatedDate = dateFormat.parse(jsonObject.getString("updatedAt"));

                        Timestamp createdAt = new Timestamp(createdDate.getTime());
                        Timestamp updatedAt = new Timestamp(updatedDate.getTime());

                        Notification newNotification = new Notification(id, uId, title, message, type, status, createdAt, updatedAt);

                        // Send the new notification to the ViewModel
                        notificationViewModel.addNewNotification(newNotification);  // Update the ViewModel with the new notification

                        Log.d("SocketManager", "Nhận thông báo mới: " + newNotification.toString());
                    }
                } catch (Exception e) {
                    Log.e("SocketManager", "Lỗi khi nhận thông báo mới: ", e);
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Gửi thông báo
    public static void sendNotification(String userId, String title, String message, String type) {
        JSONObject notification = new JSONObject();
        try {
            notification.put("userId", userId);
            notification.put("title", title);
            notification.put("message", message);
            notification.put("type", type);

            mSocket.emit("sendNotification", notification);  // Gửi thông báo
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tham gia phòng chat
    public static void joinChat(String chatId) {
        if (mSocket != null && mSocket.connected()) {
            mSocket.emit("joinChat", chatId);
            Log.d("SocketManager", "Đã tham gia phòng chat: " + chatId);
        }
    }

    // Rời phòng chat
    public static void leaveChat(String chatId) {
        if (mSocket != null && mSocket.connected()) {
            mSocket.emit("leaveChat", chatId);
            Log.d("SocketManager", "Đã rời phòng chat: " + chatId);
        }
    }

    // Gửi tin nhắn
    public static void sendMessage(JSONObject message) {
        if (mSocket != null && mSocket.connected()) {
            mSocket.emit("sendMessage", message);
            Log.d("SocketManager", "Đã gửi tin nhắn: " + message.toString());
        }
    }

    // Lắng nghe tin nhắn đến
    public static void setOnMessageReceivedListener(Emitter.Listener listener) {
        Log.d("Socket", "Setting messageReceived listener");

        messageListener = listener;
        if (mSocket != null) {
            mSocket.on("messageReceived", listener);
            Log.d("Socket", "Listener registered for messageReceived");
        } else {
            Log.e("Socket", "Socket is null");
        }
    }

    // Lắng nghe tất cả tin nhắn tới store
    public static void setOnStoreMessageReceivedListener(Emitter.Listener listener) {
        Log.d("Socket", "Setting StoreMessageReceived listener");

        messageListener = listener;
        if (mSocket != null) {
            mSocket.on("storeNewMessage", listener);
            Log.d("Socket", "Listener registered for StoreMessageReceived");
        } else {
            Log.e("Socket", "Socket is null");
        }
    }

    // Lắng nghe xóa tin nhắn
    public static void setOnMessageDeletedListener(Emitter.Listener listener) {
        messageDeletedListener = listener;
        if (mSocket != null) {
            mSocket.on("messageDeleted", listener);
        }
    }

    // Xoá tin nhắn (emit id)
    public static void deleteMessage(String chatId) {
        if (mSocket != null && mSocket.connected()) {
            mSocket.emit("deleteMessage", chatId);
            Log.d("SocketManager", "Đã gửi yêu cầu xoá tin nhắn cho phòng: " + chatId);
        }
    }

    public static void joinOrder(String orderId) {
        if (mSocket != null && mSocket.connected()) {
            mSocket.emit("joinOrder", orderId);
            Log.d("SocketManager", "Đã tham gia order: " + orderId);
        }
    }

    public static void leaveOrder(String orderId) {
        if (mSocket != null && mSocket.connected()) {
            mSocket.emit("leaveOrder", orderId);
            Log.d("SocketManager", "Đã rời order: " + orderId);
        }
    }

    public static void sendLocation(JSONObject location) {
        if (mSocket != null && mSocket.connected()) {
            mSocket.emit("sendLocation", location);
            Log.d("SocketManager", "Đã gửi tin nhắn: " + location.toString());
        }
    }

    public static void setOnLocationUpdatedListener(Emitter.Listener listener) {
        messageListener = listener;
        if (mSocket != null) {
            mSocket.on("updateLocation", listener);
        }
    }

    // Đóng kết nối
    public static void disconnectSocket() {
        if (mSocket != null && mSocket.connected()) {
            mSocket.disconnect();
        }
    }

    public static boolean isConnected() {
        return mSocket != null && mSocket.connected();
    }

}
