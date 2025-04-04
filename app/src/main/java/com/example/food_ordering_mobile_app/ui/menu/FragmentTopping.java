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
import com.example.food_ordering_mobile_app.adapters.topping.ToppingAdapter;
import com.example.food_ordering_mobile_app.models.topping.ToppingGroup;
import com.example.food_ordering_mobile_app.utils.Resource;
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
}