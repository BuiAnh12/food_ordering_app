package com.example.food_ordering_mobile_app.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.OuterDishAdapter;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.DishCategory;

import java.util.ArrayList;
import java.util.List;

public class FragmentDishes extends Fragment {

    private RecyclerView recyclerView;
    private OuterDishAdapter adapter;
    private List<DishCategory> dishCategories;

    public FragmentDishes() {
        // Required empty public constructor
    }

    public static FragmentDishes newInstance() {
        return new FragmentDishes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dishes, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadExampleData();
        adapter = new OuterDishAdapter(dishCategories);
        recyclerView.setAdapter(adapter);
        Button addDishButton = view.findViewById(R.id.addDish);
        addDishButton.setOnClickListener(this::addDish);
        Button addCategory = view.findViewById(R.id.addCategory);
        addCategory.setOnClickListener(this::addCategory);

        return view;
    }

    private void loadExampleData() {
        dishCategories = new ArrayList<>();

        List<Dish> mainDishes = new ArrayList<>();
        mainDishes.add(new Dish("1", "Grilled Chicken", "Delicious grilled chicken with spices", 150000, "chicken.jpg", true));
        mainDishes.add(new Dish("2", "Beef Steak", "Juicy beef steak with sauce", 250000, "steak.jpg", true));

        List<Dish> drinks = new ArrayList<>();
        drinks.add(new Dish("3", "Lemon Juice", "Refreshing lemon juice", 50000, "lemon.jpg", true));
        drinks.add(new Dish("4", "Iced Coffee", "Vietnamese iced coffee", 40000, "coffee.jpg", true));

        dishCategories.add(new DishCategory("Main Dishes", mainDishes));
        dishCategories.add(new DishCategory("Drinks", drinks));
    }

    public void addDish(View view) {
        // Create a new instance of the fragment you want to load
        FragmentAddDish fragment = FragmentAddDish.newInstance("1", "2");

        // Get FragmentManager (make sure this code runs inside an Activity or Fragment)
        FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();

        // Begin transaction and replace the container
        fragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_store_main, fragment)  // Make sure R.id.fragment_container exists in your layout
                .addToBackStack(null)  // Adds to back stack so user can go back
                .commit();
    }

    public void addCategory(View view) {
        // Create a new instance of the fragment you want to load
        FragmentCategoryDetail fragment = FragmentCategoryDetail.newInstance();

        // Get FragmentManager (make sure this code runs inside an Activity or Fragment)
        FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();

        // Begin transaction and replace the container
        fragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_store_main, fragment)  // Make sure R.id.fragment_container exists in your layout
                .addToBackStack(null)  // Adds to back stack so user can go back
                .commit();
    }

}
