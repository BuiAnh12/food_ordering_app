package com.example.food_ordering_mobile_app.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.food_ordering_mobile_app.models.ApiResponse;
import com.example.food_ordering_mobile_app.models.category.Category;
import com.example.food_ordering_mobile_app.network.RetrofitClient;
import com.example.food_ordering_mobile_app.network.services.CategoryService;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.utils.SharedPreferencesHelper;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {
    private final CategoryService categoryService;
    private final SharedPreferencesHelper sharedPreferencesHelper;

    public CategoryRepository(Context context) {
        categoryService = RetrofitClient.getClient(context).create(CategoryService.class);
        sharedPreferencesHelper = new SharedPreferencesHelper(context);
    }

    public LiveData<Resource<List<Category>>> getAllCategories(Integer limit, Integer page, String name) {
        MutableLiveData<Resource<List<Category>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        String storeId = sharedPreferencesHelper.getStoreId();

        categoryService.getAllCategories(storeId, limit, page, name).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success("Lấy danh sách danh mục thành công!", response.body().getData()));
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ?
                                response.errorBody().string() : "Lỗi không xác định!";
                        result.setValue(Resource.error(errorMessage, null));
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi kết nối", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable throwable) {
                Log.e("CategoryRepository", "Lỗi khi lấy danh sách danh mục: " + throwable.getMessage(), throwable);
                result.setValue(Resource.error("Lỗi kết nối", null));
            }
        });

        return result;
    }

    public LiveData<Resource<Category>> getCategory(String categoryId) {
        MutableLiveData<Resource<Category>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        categoryService.getCategory(categoryId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiResponse<Category>> call, Response<ApiResponse<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success("Lấy danh mục thành công!", response.body().getData()));
                } else {
                    result.setValue(Resource.error("Không thể lấy danh mục", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Category>> call, Throwable throwable) {
                Log.e("CategoryRepository", "Lỗi khi lấy danh mục: " + throwable.getMessage(), throwable);
                result.setValue(Resource.error("Lỗi kết nối", null));
            }
        });

        return result;
    }

    public LiveData<Resource> createCategory(Category category) {
        MutableLiveData<Resource> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        String storeId = sharedPreferencesHelper.getStoreId();

        categoryService.createCategory(storeId, category).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    result.setValue(Resource.success("Thêm danh mục thành công!", null));
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ?
                                response.errorBody().string() : "Lỗi không xác định!";
                        result.setValue(Resource.error(errorMessage, null));
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi kết nối", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<Void>> updateCategory(String categoryId, Category category) {
        MutableLiveData<Resource<Void>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        categoryService.updateCategory(categoryId, category).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    result.setValue(Resource.success("Cập nhật danh mục thành công!", null));
                } else {
                    result.setValue(Resource.error("Không thể cập nhật danh mục", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                Log.e("CategoryRepository", "Lỗi khi cập nhật danh mục: " + throwable.getMessage(), throwable);
                result.setValue(Resource.error("Lỗi kết nối", null));
            }
        });

        return result;
    }

    public LiveData<Resource<Void>> deleteCategory(String categoryId) {
        MutableLiveData<Resource<Void>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        categoryService.deleteCategory(categoryId).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    result.setValue(Resource.success("Xóa danh mục thành công!", null));
                } else {
                    try {
                        String errorMessage = "Không thể xóa danh mục";

                        if (response.errorBody() != null) {
                            String errorJson = response.errorBody().string();
                            JSONObject errorObj = new JSONObject(errorJson);
                            errorMessage = errorObj.optString("message", errorMessage);
                        }

                        result.setValue(Resource.error(errorMessage, null));
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi khi xử lý phản hồi", null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                Log.e("CategoryRepository", "Lỗi khi xóa danh mục: " + throwable.getMessage(), throwable);
                result.setValue(Resource.error("Lỗi kết nối", null));
            }
        });

        return result;
    }

}
