package com.example.food_ordering_mobile_app.ui.menu;

import static com.example.food_ordering_mobile_app.utils.Resource.Status.SUCCESS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.category.Category;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.image.Image;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.CategoryViewModel;
import com.example.food_ordering_mobile_app.viewModel.DishViewModel;
import com.example.food_ordering_mobile_app.viewModel.UploadViewModel;

import java.util.ArrayList;
import java.util.List;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import retrofit2.http.Url;

public class FragmentDishDetail extends Fragment {

    private static final String DISH_ID = "dish_id";

    private String dishId;
    private Dish dish;
    private List<Category> categories;

    private DishViewModel dishViewModel;
    private CategoryViewModel categoryViewModel;
    private ArrayAdapter<Category> categoryArrayAdapter;

    private UploadViewModel uploadViewModel;

    private boolean isDishLoaded = false;
    private boolean isCategoryLoaded = false;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri; // store the selected image for upload later
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 100;
    private String originalImageUrl;



    public FragmentDishDetail() {}

    public static FragmentDishDetail newInstance(String param1) {
        FragmentDishDetail fragment = new FragmentDishDetail();
        Bundle args = new Bundle();
        args.putString(DISH_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dishId = getArguments().getString(DISH_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dish_detail, container, false);
        TextView goBackBtn = view.findViewById(R.id.backBtn);
        Button saveBtn = view.findViewById(R.id.active_button);
        Button deleteBtn = view.findViewById(R.id.deactive_button);
        Button editImageBtn = view.findViewById(R.id.editImageButton);
        editImageBtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                // Xin quyền
                requestPermissions(new String[] {
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
            } else {
                openImagePicker();
            }
        });


        goBackBtn.setOnClickListener(this::goBack);
        saveBtn.setOnClickListener(this::saveDish);
        deleteBtn.setOnClickListener(this::deleteDish);
        uploadViewModel = new ViewModelProvider(this).get(UploadViewModel.class);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories(20, 1, "");
        categoryViewModel.getAllCategoryResponse().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource.getStatus() == SUCCESS && listResource.getData() != null) {
                categories = listResource.getData();
                isCategoryLoaded = true;
                Log.d("FragmentDishDetail", "Categories loaded");
                tryLoadData();
            }
        });

        dishViewModel = new ViewModelProvider(this).get(DishViewModel.class);
        dishViewModel.getDish(dishId);
        dishViewModel.getDishResponse().observe(getViewLifecycleOwner(), dishResource -> {
            if (dishResource.getStatus() == SUCCESS && dishResource.getData() != null) {
                dish = dishResource.getData();
                isDishLoaded = true;
                Log.d("FragmentDishDetail", "Dish loaded");
                tryLoadData();
            }
        });

        dishViewModel.getUpdateDishResponse().observe(getViewLifecycleOwner(), dishResource -> {
            switch (dishResource.getStatus()) {
                case SUCCESS:
                    Toast.makeText(getContext(), "Cập nhật món ăn thành công", Toast.LENGTH_SHORT).show();
                    goBack(null);
                    break;
                case ERROR:
                    Toast.makeText(getContext(), "Cập nhật thất bại: " + dishResource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            ImageView dishImage = getView().findViewById(R.id.foodImage);

            Glide.with(this)
                    .load(selectedImageUri)
                    .apply(new RequestOptions().override(150, 150).centerCrop())
                    .into(dishImage);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh món ăn"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(getContext(), "Bạn cần cấp quyền để chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void deleteDish(View view) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa món ăn này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    // User confirmed deletion
                    performDeleteDish();
                })
                .setNegativeButton("Hủy", (dialog, which) -> {
                    // Do nothing, just dismiss
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    private void performDeleteDish() {
        if (dishId == null) {
            Toast.makeText(getContext(), "Không tìm thấy món ăn để xóa", Toast.LENGTH_SHORT).show();
            return;
        }

        dishViewModel.deleteDish(dishId);

        dishViewModel.deleteDishResponse().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case SUCCESS:
                    Toast.makeText(getContext(), "Xóa món ăn thành công", Toast.LENGTH_SHORT).show();
                    goBack(null);
                    break;
                case ERROR:
                    Toast.makeText(getContext(), "Xóa món ăn thất bại: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }



    private void tryLoadData() {
        if (isDishLoaded && isCategoryLoaded) {
            loadData();
        }
    }

    private void loadData() {
        EditText name = getView().findViewById(R.id.nameInput);
        EditText price = getView().findViewById(R.id.priceInput);
        EditText description = getView().findViewById(R.id.descriptionInput);
        ImageView dish_image = getView().findViewById(R.id.foodImage);
        Spinner categorySpinner = getView().findViewById(R.id.categorySpinner);

        Image displayImage = dish.getImage();
        if (displayImage != null && displayImage.getUrl() != null) {
            originalImageUrl = displayImage.getUrl();
        }

        categoryArrayAdapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_dropdown_item,
                categories
        );
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryArrayAdapter);

        // Set selected category
        String selectedCategoryId = dish.getCategory().getId();
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equals(selectedCategoryId)) {
                categorySpinner.setSelection(i);
                break;
            }
        }

        name.setText(dish.getName());
        price.setText(String.valueOf(dish.getPrice()));
        description.setText(dish.getDescription());
        displayImage = dish.getImage();
        Glide.with(dish_image.getContext())
                .load(displayImage != null && displayImage.getUrl() != null ? displayImage.getUrl() : R.drawable.menu_1)
                .apply(new RequestOptions()
                        .override(150, 150) // Match the ImageView size
                        .centerCrop()
                        .placeholder(R.drawable.menu_1)
                        .error(R.drawable.menu_1))
                .into(dish_image);

    }


    public void goBack(View view) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    public static <T> void observeOnce(final androidx.lifecycle.LiveData<T> liveData, final androidx.lifecycle.LifecycleOwner lifecycleOwner, final androidx.lifecycle.Observer<T> observer) {
        liveData.observe(lifecycleOwner, new androidx.lifecycle.Observer<T>() {
            @Override
            public void onChanged(T t) {
                liveData.removeObserver(this);
                observer.onChanged(t);
            }
        });
    }

    private void saveDish(View view) {
        EditText nameInput = getView().findViewById(R.id.nameInput);
        EditText priceInput = getView().findViewById(R.id.priceInput);
        EditText descriptionInput = getView().findViewById(R.id.descriptionInput);
        Spinner categorySpinner = getView().findViewById(R.id.categorySpinner);

        String name = nameInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();
        String priceStr = priceInput.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        Category selectedCategory = (Category) categorySpinner.getSelectedItem();
        if (selectedCategory == null) {
            Toast.makeText(getContext(), "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show();
            return;
        }

        dish.setName(name);
        dish.setPrice(price);
        if (!description.isEmpty()) {
            dish.setDescription(description);
        }
        dish.setCategory(selectedCategory);

        // 🖼 Nếu người dùng đã chọn ảnh mới, upload trước
        if (selectedImageUri != null && (originalImageUrl == null || !selectedImageUri.toString().equals(originalImageUrl))) {
            List<Uri> imageUris = new ArrayList<>();
            imageUris.add(selectedImageUri);

            // 1. Observe before calling uploadImages
            uploadViewModel.getUploadImagesResponse().observe(getViewLifecycleOwner(), resource -> {
                switch (resource.getStatus()) {
                    case SUCCESS:
                        List<Image> images = resource.getData();
                        Log.d("FragmentDishDetail", "Image uploaded successfully");
                        Image uploadedImage = images.get(0);
                        dish.setImage(uploadedImage);
                        dishViewModel.updateDish(dish.getId(), dish);
                        break;

                    case ERROR:
                        Toast.makeText(getContext(), "Upload ảnh thất bại: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            });

            uploadViewModel.uploadImages(imageUris, getContext());

        }
        else {
            dishViewModel.updateDish(dish.getId(), dish);
        }
    }

}
