package com.example.food_ordering_mobile_app.adapters.dish;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.dish.Dish;

import java.util.List;

public class DishToOrderAdapter extends RecyclerView.Adapter<DishToOrderAdapter.DishViewHolder> {

    public interface OnDishClickListener {
        void onDishClick(Dish dish);
    }

    private List<Dish> dishList;
    private OnDishClickListener listener;

    public DishToOrderAdapter(List<Dish> dishList, OnDishClickListener listener) {
        this.dishList = dishList;
        this.listener = listener;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishList = dishes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_dish, parent, false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        Dish dish = dishList.get(position);
        holder.bind(dish);
    }

    @Override
    public int getItemCount() {
        return dishList != null ? dishList.size() : 0;
    }

    class DishViewHolder extends RecyclerView.ViewHolder {
        TextView dishName;
        TextView dishPrice;

        DishViewHolder(View itemView) {
            super(itemView);
            dishName = itemView.findViewById(R.id.dish_name);
            dishPrice = itemView.findViewById(R.id.dish_price);
        }

        void bind(Dish dish) {
            dishName.setText(dish.getName());
            dishPrice.setText(String.valueOf(dish.getPrice()));
            itemView.setOnClickListener(v -> listener.onDishClick(dish));
        }
    }
}
