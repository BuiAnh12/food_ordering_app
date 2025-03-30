package com.example.food_ordering_mobile_app.ui.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.ToppingAdapter;
import com.example.food_ordering_mobile_app.models.topping.Topping;
import com.example.food_ordering_mobile_app.models.topping.ToppingGroup;

import java.util.ArrayList;
import java.util.List;

public class FragmentTopping extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private RecyclerView recyclerView;
    private ToppingAdapter adapter;

    private List<ToppingGroup> toppingGroups;


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

        loadExampleData();
        adapter = new ToppingAdapter(toppingGroups);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void loadExampleData() {
        toppingGroups = new ArrayList<>();

        List<Topping> milkTeaTopping = new ArrayList<>();
        milkTeaTopping.add(new Topping("1231", "Bubble Dice 1", 12321));
        milkTeaTopping.add(new Topping( "1232", "Bubble Dice 2", 12321));

        List<Topping> pizzaTopping = new ArrayList<>();
        pizzaTopping.add(new Topping("1231", "Additional Cheese", 12321));
        pizzaTopping.add(new Topping( "1232", "Additional Sauce", 12321));

        toppingGroups.add(new ToppingGroup("123", milkTeaTopping, "Milk Tea Topping","1231"));
        toppingGroups.add(new ToppingGroup("124", pizzaTopping, "Pizza Topping","1231"));
    }
}