package com.example.food_ordering_mobile_app.ui.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.ToppingAdapter;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.dish.DishCategory;
import com.example.food_ordering_mobile_app.models.topping.Topping;
import com.example.food_ordering_mobile_app.models.topping.ToppingGroup;
import com.example.food_ordering_mobile_app.utils.Resource;
import com.example.food_ordering_mobile_app.viewModel.DishViewModel;
import com.example.food_ordering_mobile_app.viewModel.ToppingViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentTopping extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private RecyclerView recyclerView;
    private ToppingAdapter adapter;
    private ToppingViewModel toppingViewModel;

    private List<ToppingGroup> toppingGroups = new ArrayList<>();


    public FragmentTopping() {
        // Required empty public constructor
    }

    public static FragmentTopping newInstance() {
        return new FragmentTopping();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topping, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ToppingAdapter(toppingGroups);
        recyclerView.setAdapter(adapter);

        toppingViewModel = new ViewModelProvider(this).get(ToppingViewModel.class);
        toppingViewModel.getAllToppings(10, 1);

        toppingViewModel.getAlllToppingsResponse().observe(getViewLifecycleOwner(), new Observer<Resource<List<ToppingGroup>>>() {
            @Override
            public void onChanged(Resource<List<ToppingGroup>> listResource) {
                switch (listResource.getStatus()) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        if (listResource.getData() != null) {
                            toppingGroups.clear();
                            Log.d("FragmentToppingFragment", "Data: " + Arrays.toString(listResource.getData().toArray()));
                            toppingGroups.addAll(listResource.getData());
                            adapter.notifyDataSetChanged();
                        }
                        break;
                    case ERROR:
                        Log.e("FragmentToppingFragment", "Error: " + listResource.getMessage());
                        Toast.makeText(getContext(), listResource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        return view;
    }

    private void loadExampleData() {
//        toppingGroups = new ArrayList<>();
//
//        List<Topping> milkTeaTopping = new ArrayList<>();
//        milkTeaTopping.add(new Topping("1231", "Bubble Dice 1", 12321));
//        milkTeaTopping.add(new Topping( "1232", "Bubble Dice 2", 12321));
//
//        List<Topping> pizzaTopping = new ArrayList<>();
//        pizzaTopping.add(new Topping("1231", "Additional Cheese", 12321));
//        pizzaTopping.add(new Topping( "1232", "Additional Sauce", 12321));
//
//        toppingGroups.add(new ToppingGroup("123", milkTeaTopping, "Milk Tea Topping","1231"));
//        toppingGroups.add(new ToppingGroup("124", pizzaTopping, "Pizza Topping","1231"));
    }
}