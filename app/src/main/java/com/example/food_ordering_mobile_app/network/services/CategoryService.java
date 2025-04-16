package com.example.food_ordering_mobile_app.network.services;
import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.category.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CategoryService {
    @GET("/api/v1/store/{store_id}/category")
    Call<ApiResponse<List<Category>>> getAllCategories(@Path("store_id") String storeId,
                                                       @Query("limit") Integer limit,
                                                       @Query("page") Integer page,
                                                       @Query("name") String name);

    @GET("/api/v1/store/category/{category_id}")
    Call<ApiResponse<Category>> getCategory(@Path("category_id") String categoryId);

    @POST("/api/v1/store/{store_id}/category/add")
    Call<ApiResponse> createCategory(@Path("store_id") String storeId, @Body Category category);

    @PUT("/api/v1/store/category/{category_id}")
    Call<ApiResponse> updateCategory(@Path("category_id") String categoryId, @Body Category category);

    @DELETE("/api/v1/store/category/{category_id}")
    Call<ApiResponse> deleteCategory(@Path("category_id") String categoryId);
}
