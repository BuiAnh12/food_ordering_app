package com.example.food_ordering_mobile_app.ui.menu;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.category.CategoryAdapter;
import com.example.food_ordering_mobile_app.models.category.Category;
import com.example.food_ordering_mobile_app.repository.CategoryRepository;
import com.example.food_ordering_mobile_app.utils.Resource;

import java.util.ArrayList;
import java.util.List;

public class FragmentCategoryDetail extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<Category> categoryList;
    private CategoryRepository categoryRepository;
    private TextView btnAddCategory;

    public FragmentCategoryDetail() {
        // Required empty public constructor
    }

    public static FragmentCategoryDetail newInstance() {
        return new FragmentCategoryDetail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_detail, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryList = new ArrayList<>();
        adapter = new CategoryAdapter(categoryList);
        recyclerView.setAdapter(adapter);

        btnAddCategory = view.findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(v -> showAddCategoryDialog());

        adapter.setOnCategoryDeleteListener((category, position) -> {
            showDeleteConfirmationDialog(category, position);
        });

        categoryRepository = new CategoryRepository(requireContext());
        loadCategories();

        return view;
    }

    private void loadCategories() {
        categoryRepository.getAllCategories(20, 1, "")
                .observe(getViewLifecycleOwner(), resource -> {
                    switch (resource.getStatus()) {
                        case SUCCESS:
                            if (resource.getData() != null) {
                                categoryList.clear();
                                categoryList.addAll(resource.getData());
                                adapter.notifyDataSetChanged();
                            }
                            break;
                        case ERROR:
                            Toast.makeText(getContext(), "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                            break;
                        case LOADING:
                            // Optional: Show loading state if needed
                            break;
                    }
                });
    }

    private void createCategory(String name) {
        Category newCategory = new Category();
        newCategory.setName(name);

        categoryRepository.createCategory(newCategory).observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case SUCCESS:
                    loadCategories(); // Refresh list after creation
                    Toast.makeText(getContext(), "Thêm danh mục thành công!", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    Toast.makeText(getContext(), "Lỗi khi thêm: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case LOADING:
                    // Optional: show loading state
                    break;
            }
        });
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thêm danh mục mới");

        final EditText input = new EditText(getContext());
        input.setHint("Nhập tên danh mục");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String categoryName = input.getText().toString().trim();
            if (!categoryName.isEmpty()) {
                createCategory(categoryName);
            } else {
                Toast.makeText(getContext(), "Tên danh mục không được để trống", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showDeleteConfirmationDialog(Category category, int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa danh mục \"" + category.getName() + "\"?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    categoryRepository.deleteCategory(category.getId())
                            .observe(getViewLifecycleOwner(), deleteResource -> {
                                switch (deleteResource.getStatus()) {
                                    case SUCCESS:
                                        categoryList.remove(position);
                                        adapter.notifyItemRemoved(position);
                                        Toast.makeText(getContext(), "Xóa danh mục thành công!", Toast.LENGTH_SHORT).show();
                                        break;
                                    case ERROR:
                                        Toast.makeText(getContext(), "Lỗi khi xóa: " + deleteResource.getMessage(), Toast.LENGTH_SHORT).show();
                                        break;
                                    case LOADING:
                                        // Optional: show loading
                                        break;
                                }
                            });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

}
