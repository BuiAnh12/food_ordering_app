package com.example.food_ordering_mobile_app.ui.menu;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.category.Category;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.CategoryViewModel;
import com.example.food_ordering_mobile_app.viewModel.DishViewModel;

import java.util.List;

public class FragmentDishDetail extends Fragment {

    private static final String DISH_ID = "dish_id";

    private String dishId;
    private Dish dish;
    private List<Category> categories;

    private DishViewModel dishViewModel;
    private CategoryViewModel categoryViewModel;
    private ArrayAdapter<Category> categoryArrayAdapter;

    private boolean isDishLoaded = false;
    private boolean isCategoryLoaded = false;

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
        goBackBtn.setOnClickListener(this::goBack);
        saveBtn.setOnClickListener(this::saveDish);
        deleteBtn.setOnClickListener(this::deleteDish);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories(20, 1, "");
        categoryViewModel.getAllCategoryResponse().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource.getStatus() == Resource.Status.SUCCESS && listResource.getData() != null) {
                categories = listResource.getData();
                isCategoryLoaded = true;
                Log.d("FragmentDishDetail", "Categories loaded");
                tryLoadData();
            }
        });

        dishViewModel = new ViewModelProvider(this).get(DishViewModel.class);
        dishViewModel.getDish(dishId);
        dishViewModel.getDishResponse().observe(getViewLifecycleOwner(), dishResource -> {
            if (dishResource.getStatus() == Resource.Status.SUCCESS && dishResource.getData() != null) {
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
        Spinner categorySpinner = getView().findViewById(R.id.categorySpinner);

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
    }

    public void goBack(View view) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
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

        // Update dish
        dish.setName(name);
        if (!description.isEmpty()) {
            dish.setDescription(description);
        }
        dish.setPrice(price);
        dish.setCategory(selectedCategory);

        dishViewModel.updateDish(dish.getId(), dish);
    }
}
