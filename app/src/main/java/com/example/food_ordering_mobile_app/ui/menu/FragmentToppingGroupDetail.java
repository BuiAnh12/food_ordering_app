package com.example.food_ordering_mobile_app.ui.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.ToppingDetailAdapter;
import com.example.food_ordering_mobile_app.models.topping.Topping;

import java.util.ArrayList;
import java.util.List;

public class FragmentToppingGroupDetail extends Fragment {

    private RecyclerView recyclerView;
    private ToppingDetailAdapter adapter;
    private List<Topping> toppingList;

    public FragmentToppingGroupDetail() {
        // Required empty public constructor
    }

    public static FragmentToppingGroupDetail newInstance() {
        return new FragmentToppingGroupDetail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topping_group_detail, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadExampleData();
        adapter = new ToppingDetailAdapter(toppingList);
        recyclerView.setAdapter(adapter);
        TextView goBackBtn = view.findViewById(R.id.backBtn);
        goBackBtn.setOnClickListener(this::goBack);
        return view;
    }

    private void loadExampleData() {
        toppingList = new ArrayList<>();
        toppingList.add(new Topping("1231", "Bubble Dice 1", 12321));
        toppingList.add(new Topping( "1232", "Bubble Dice 2", 12321));
        toppingList.add(new Topping("1231", "Additional Cheese", 12321));
        toppingList.add(new Topping( "1232", "Additional Sauce", 12321));

    }

    public void goBack(View view) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}