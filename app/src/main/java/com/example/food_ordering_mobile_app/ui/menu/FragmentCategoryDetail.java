package com.example.food_ordering_mobile_app.ui.menu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.CategoryAdapter;
import com.example.food_ordering_mobile_app.models.category.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentCategoryDetail extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<Category> categoryList;

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

        loadExampleData();
        adapter = new CategoryAdapter(categoryList);
        recyclerView.setAdapter(adapter);

        // Attach ItemTouchHelper for drag-and-drop functionality
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void loadExampleData() {
        categoryList = new ArrayList<>();

        categoryList.add(new Category("123a", "Main Dishes", 123, 1));
        categoryList.add(new Category("123b", "Appetizers", 124, 2));
        categoryList.add(new Category("123c", "Drinks", 125, 3));
        categoryList.add(new Category("123d", "Desserts", 126, 4));
        categoryList.add(new Category("123e", "Side Dishes", 127, 5));
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            // Swap items and notify adapter
            Collections.swap(categoryList, fromPosition, toPosition);
            adapter.notifyItemMoved(fromPosition, toPosition);

            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // No swipe action needed
        }
    };
}
