package com.example.food_ordering_mobile_app.network.services;


import com.example.food_ordering_mobile_app.models.image.Image;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadService {
    @Multipart
    @POST("upload/avatar")
    Call<ResponseBody> uploadAvatar(@Part MultipartBody.Part file);
    @Multipart
    @POST("upload/images")
    Call<List<Image>> uploadImages(@Part List<MultipartBody.Part> files);

    @DELETE("upload/delete-file")
    Call<ResponseBody> deleteFile();
}