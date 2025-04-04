package com.example.food_ordering_mobile_app.ui.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.OuterDishAdapter;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.DishCategory;
import com.example.food_ordering_mobile_app.models.order.Order;
import com.example.food_ordering_mobile_app.models.order.OrderDateStatusGroup;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.DishViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentDishes extends Fragment {

    private RecyclerView dishRecyclerView;
    private OuterDishAdapter adapter;
    private List<DishCategory> dishCategories = new ArrayList<>();
    private DishViewModel dishViewModel;

    public FragmentDishes() {
        // Required empty public constructor
    }

    public static FragmentDishes newInstance() {
        return new FragmentDishes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_dishes, container, false);
        dishRecyclerView = view.findViewById(R.id.recyclerView);
        dishRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new OuterDishAdapter(dishCategories);
        dishRecyclerView.setAdapter(adapter);

        dishViewModel = new ViewModelProvider(this).get(DishViewModel.class);
        dishViewModel.getAllDishes("", 10, 1);

        Button addDishButton = view.findViewById(R.id.addDish);
        addDishButton.setOnClickListener(this::addDish);
        Button addCategory = view.findViewById(R.id.addCategory);
        addCategory.setOnClickListener(this::addCategory);

        dishViewModel.getAllDishesResponse().observe(getViewLifecycleOwner(), new Observer<Resource<List<Dish>>>() {
            @Override
            public void onChanged(Resource<List<Dish>> listResource) {
                switch (listResource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (listResource.getData() != null) {
                            dishCategories.clear();
                            Log.d("FragmentHistoryOrder", "Data: " + Arrays.toString(listResource.getData().toArray()));
                            List<Dish> dishes = listResource.getData();
                            List<DishCategory> dishCategoriesList = DishCategory.groupDishesByCategory(dishes);
                            dishCategories.addAll(dishCategoriesList);
                            adapter.notifyDataSetChanged();
                        }
                        break;
                    case ERROR:
                        Log.e("FragmentConfirmOrder", "Error: " + listResource.getMessage());
                        Toast.makeText(getContext(), listResource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        return view;
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
