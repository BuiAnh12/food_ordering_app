package com.example.food_ordering_mobile_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.dish.DishCategory;

import java.util.List;

public class OuterDishAdapter extends RecyclerView.Adapter<OuterDishAdapter.ViewHolder> {
    private List<DishCategory> dishCategories;

    public OuterDishAdapter(List<DishCategory> dishCategories) {
        this.dishCategories = dishCategories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_dish_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OuterDishAdapter.ViewHolder holder, int position) {
        DishCategory dishCategory = dishCategories.get(position);
        holder.catText.setText(dishCategory.getCategoryName());

        InnerDishAdapter childAdapter = new InnerDishAdapter(dishCategory.getItems());
        holder.innerRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.innerRecyclerView.setAdapter(childAdapter);
    }

    @Override
    public int getItemCount() {
        return dishCategories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView catText;
        RecyclerView innerRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            catText = itemView.findViewById(R.id.categoryText);
            innerRecyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }
}
