package com.example.food_ordering_mobile_app.ui.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.category.Category;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.CategoryViewModel;
import com.example.food_ordering_mobile_app.viewModel.DishViewModel;

import java.util.List;

public class FragmentAddDish extends Fragment {

    private DishViewModel dishViewModel;
    private CategoryViewModel categoryViewModel;
    private List<Category> categories;
    private ArrayAdapter<Category> categoryArrayAdapter;

    public FragmentAddDish() {
        // Required empty public constructor
    }

    public static FragmentAddDish newInstance() {
        return new FragmentAddDish();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_dish, container, false);

        TextView goBackBtn = view.findViewById(R.id.backBtn);
        Button saveBtn = view.findViewById(R.id.active_button);

        goBackBtn.setOnClickListener(this::goBack);
        saveBtn.setOnClickListener(this::createDish);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        dishViewModel = new ViewModelProvider(this).get(DishViewModel.class);

        // Load categories
        categoryViewModel.getAllCategories(20, 1, "");
        categoryViewModel.getAllCategoryResponse().observe(getViewLifecycleOwner(), new Observer<Resource<List<Category>>>() {
            @Override
            public void onChanged(Resource<List<Category>> listResource) {
                if (listResource.getStatus() == Resource.Status.SUCCESS && listResource.getData() != null) {
                    categories = listResource.getData();
                    Spinner categorySpinner = view.findViewById(R.id.categorySpinner);
                    categoryArrayAdapter = new ArrayAdapter<>(
                            getContext(),
                            R.layout.spinner_dropdown_item,
                            categories
                    );
                    categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(categoryArrayAdapter);
                }
            }
        });

        dishViewModel.getCreateDishResponse().observe(getViewLifecycleOwner(), dishResource -> {
            switch (dishResource.getStatus()) {
                case SUCCESS:
                    Toast.makeText(getContext(), "Tạo món ăn thành công", Toast.LENGTH_SHORT).show();
                    goBack(null);
                    break;
                case ERROR:
                    Toast.makeText(getContext(), "Tạo món ăn thất bại: " + dishResource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case LOADING:
                    // Optional loading indicator
                    break;
            }
        });

        return view;
    }

    public void goBack(View view) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void createDish(View view) {
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

        // Create new dish
        Dish newDish = new Dish();
        newDish.setName(name);
        newDish.setPrice(price);
        newDish.setDescription(description);
        newDish.setCategory(selectedCategory);
        dishViewModel.createDish(newDish);
    }
}
