package com.example.food_ordering_mobile_app.ui.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.chat.MessageAdapter;
import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.image.Image;
import com.example.food_ordering_mobile_app.models.chat.Message;
import com.example.food_ordering_mobile_app.models.chat.MessageResponse;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.network.SocketManager;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;
import com.example.food_ordering_mobile_app.viewModel.AuthViewModel;
import com.example.food_ordering_mobile_app.viewModel.ChatViewModel;
import com.example.food_ordering_mobile_app.viewModel.UploadViewModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
public class DetailMessageActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private ChatViewModel chatViewModel;
    private UploadViewModel uploadViewModel;
    private AuthViewModel authViewModel;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private RecyclerView chatRecyclerView;
    private EditText etMessageInput;
    private ImageView sendMessage, ivUserAvatar, sendImage;
    private TextView tvUserName, tvTime;
    private String chatId;
    private static final int PICK_IMAGES_REQUEST = 1;

    private Store store;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_message);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        ivUserAvatar = findViewById(R.id.ivUserAvatar);
        tvUserName = findViewById(R.id.tvUserName);
        //tvTime = findViewById(R.id.tvTime);
        etMessageInput = findViewById(R.id.etMessageInput);
        sendMessage = findViewById(R.id.sendMessage);
        sendImage = findViewById(R.id.sendImage);

        chatId = getIntent().getStringExtra("chatId") != null ? getIntent().getStringExtra("chatId") : "";

        uploadViewModel = new ViewModelProvider(this).get(UploadViewModel.class);
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        setupAllMessages();

        sendImage.setOnClickListener(view -> openImagePicker());
        authViewModel.getOwnStore();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, this, messageList);
        chatRecyclerView.setAdapter(messageAdapter);
        chatRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = layoutManager.getItemCount();

                    // When the last item is fully visible, trigger reload (get new messages)
                    if (lastVisible == totalItemCount - 1 && dy > 0) {
                        // dy > 0 ensures it's a scroll up
                        refreshData(); // Fetch new messages from server
                    }
                }
            }
        });

        SocketManager.joinChat(chatId);

        sendMessage.setOnClickListener(v -> {
            String messageContent = etMessageInput.getText().toString().trim();
            if (!messageContent.isEmpty()) {
                try {
                    Map<String, Object> data = new HashMap<>();
                    data.put("content", messageContent);

                    chatViewModel.sendMessage(chatId, data); // Let ViewModel handle server POST

                    // Optimistically add the message to the UI
//                    Message newMessage = new Message();
//                    newMessage.setContent(messageContent);
//                    User store_owner = new User();
//                    store_owner.setId(store.getOwner());
//                    newMessage.setSender(store_owner);
//                    Log.d("SUPER DEBUG",store.getOwner());
//                    newMessage.setCreatedAt(new Timestamp(System.currentTimeMillis()));
//                    messageList.add(newMessage);
//                    messageAdapter.notifyDataSetChanged();

                    // Send via socket
                    JSONObject dataObject = new JSONObject();
                    dataObject.put("content", messageContent);

                    JSONObject sendObject = new JSONObject();
                    sendObject.put("id", chatId);
                    sendObject.put("data", dataObject);

                    SocketManager.sendMessage(sendObject);

                    etMessageInput.setText("");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        SocketManager.setOnMessageReceivedListener(args  -> runOnUiThread(this::setupAllMessages));

        SocketManager.setOnMessageDeletedListener(args  -> runOnUiThread(this::setupAllMessages));

        chatViewModel.getSendMessageResponse().observe(this, new Observer<Resource<ApiResponse<Message>>>() {
            @Override
            public void onChanged(Resource<ApiResponse<Message>> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        Log.d("DetailMessage",resource.getData().getData().toString());
//                        messageList.add(resource.getData().getData());
//                        messageAdapter.notifyItemInserted(messageList.size() - 1);
//                        chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
//                        messageAdapter.notifyDataSetChanged();
//                        chatRecyclerView.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (messageList != null && !messageList.isEmpty()) {
//                                    chatRecyclerView.post(() -> {
//                                        chatRecyclerView.smoothScrollToPosition(messageList.size());
//                                    });
//                                }
//                            }
//                        });
                        setupAllMessages();
                        break;
                    case ERROR:
                        Log.d("DetailMessage","SEND FAIL");
                        break;
                }
            }
        });

        authViewModel.getOwnStoreResponse().observe(this, new Observer<Resource<Store>>() {
            @Override
            public void onChanged(Resource<Store> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        store = resource.getData();
                        Log.d("AuthViewModel", "Store data: " + resource.getData().toString());
                        break;
                    case ERROR:
                        Log.e("AuthViewModel", "Error: " + resource.getMessage());
                        break;
                }
            }
        });

        uploadViewModel.getUploadImagesResponse().observe(this, new Observer<Resource<List<Image>>>() {
            @Override
            public void onChanged(Resource<List<Image>> resource) {
                switch (resource.getStatus()) {
                    case SUCCESS:
                        List<Image> uploadedImageUrls = resource.getData();
                        if (uploadedImageUrls != null && !uploadedImageUrls.isEmpty()) {
                            Image image = uploadedImageUrls.get(0);

                            // Gửi dữ liệu qua ViewModel
                            Map<String, Object> data = new HashMap<>();
                            data.put("content", "");
                            data.put("image", image);
                            chatViewModel.sendMessage(chatId, data);

                            // Gửi dữ liệu qua Socket
                            try {
                                JSONObject dataObject = new JSONObject();
                                dataObject.put("content", "");
                                dataObject.put("image", new JSONObject(new Gson().toJson(image))); // nếu image là object

                                JSONObject sendObject = new JSONObject();
                                sendObject.put("id", chatId);
                                sendObject.put("data", dataObject);

                                SocketManager.sendMessage(sendObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case ERROR:
                        // handle error
                        break;
                    default:
                        break;
                }
            }
        });

        chatViewModel.getAllMessagesResponse().observe(this, new Observer<Resource<MessageResponse>>() {
            @Override
            public void onChanged(Resource<MessageResponse> resource) {
                switch (resource.getStatus()) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        messageList.clear();
                        messageList.addAll(resource.getData().getMessages());

                        List<User> users = resource.getData().getChat().getUsers();
                        tvUserName.setText(users.get(0).getName());
                        String avatarUrl = users.get(0).getAvatar() != null ? users.get(0).getAvatar().getUrl() : null;
                        loadRoundedImage(avatarUrl, ivUserAvatar);
//                        long timeDiff = System.currentTimeMillis() - resource.getData().getChat().getLatestMessage().getUpdatedAt().getTime();
//                        tvTime.setText(formatTimeAgo(timeDiff));


                        messageAdapter.notifyDataSetChanged();

                        chatRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                if (messageList.size() > 0) {
                                    chatRecyclerView.scrollToPosition(messageList.size() - 1);
                                }

                                // Remove listener để không bị gọi lại nhiều lần
                                chatRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        });

                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("ChatFragment", "getAllMessagesResponse Error: " + resource.getMessage());
                        break;
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                Uri uri = data.getData();
                List<Uri> selectedImageUris = new ArrayList<>();
                if (!selectedImageUris.contains(uri)) {
                    selectedImageUris.add(uri);
                }
                uploadViewModel.uploadImages(selectedImageUris, this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketManager.leaveChat(chatId);
    }

    private void setupAllMessages() {
        chatViewModel.getAllMessages(chatId);
    }

    private void refreshData() {
        swipeRefreshLayout.setRefreshing(true);
        chatViewModel.getAllMessages(chatId);
    }

    private String formatTimeAgo(long timeDiff) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDiff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff);
        long hours = TimeUnit.MILLISECONDS.toHours(timeDiff);
        long days = TimeUnit.MILLISECONDS.toDays(timeDiff);

        if (seconds < 60) return seconds + " giây trước";
        if (minutes < 60) return minutes + " phút trước";
        if (hours < 24) return hours + " giờ trước";
        return days + " ngày trước";
    }

    private void loadRoundedImage(String url, ImageView imageView) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .override(50, 50)
                .centerCrop()
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedDrawable =
                                RoundedBitmapDrawableFactory.create(imageView.getResources(), resource);
                        roundedDrawable.setCornerRadius(6);
                        imageView.setImageDrawable(roundedDrawable);
                    }
                });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(Intent.createChooser(intent, "Chọn 1 ảnh"), PICK_IMAGES_REQUEST);
    }

    public void goBack(View view) {
        finish();
    }
}