package com.example.food_ordering_mobile_app.network.services;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.chat.Chat;
import com.example.food_ordering_mobile_app.models.chat.Message;
import com.example.food_ordering_mobile_app.models.chat.MessageResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatService {
    @POST("chat/{id}")
    Call<Chat> createChat(@Path("id") String id);

    @POST("chat/{id}/store/{storeId}")
    Call<Chat> createStoreChat(@Path("id") String id, @Path("storeId") String storeId);
    @POST("message/{id}")
    Call<ApiResponse<Message>> sendMessage(@Path("id") String id, @Body Map<String, Object> data);
    @GET("chat/")
    Call<List<Chat>> getAllChats();
    @GET("message/{id}")
    Call<MessageResponse> getAllMessages(@Path("id") String id);
    @DELETE("chat/delete/{id}")
    Call<ApiResponse<String>> deleteChat(@Path("id") String id);
    @DELETE("message/delete/{id}")
    Call<ApiResponse<String>> deleteMessage(@Path("id") String id);
}
