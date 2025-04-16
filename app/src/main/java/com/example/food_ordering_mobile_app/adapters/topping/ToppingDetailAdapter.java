package com.example.food_ordering_mobile_app.adapters.topping;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.topping.Topping;

import java.util.List;

public class ToppingDetailAdapter extends RecyclerView.Adapter<ToppingDetailAdapter.ToppingViewHolder> {

    private final List<Topping> toppingList;
    private final OnEditClickListener onEditClickListener;
    private final OnDeleteClickListener onDeleteClickListener;

    public interface OnEditClickListener {
        void onEditClick(Topping topping);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Topping topping);
    }

    public ToppingDetailAdapter(List<Topping> toppingList,
                                OnEditClickListener onEditClickListener,
                                OnDeleteClickListener onDeleteClickListener) {
        this.toppingList = toppingList;
        this.onEditClickListener = onEditClickListener;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public ToppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topping_detail, parent, false);
        return new ToppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppingViewHolder holder, int position) {
        Topping topping = toppingList.get(position);
        holder.name.setText(topping.getName());
        holder.price.setText(String.format("%.0fđ", topping.getPrice()));

        // Click anywhere to edit
        holder.itemView.setOnClickListener(v -> onEditClickListener.onEditClick(topping));

        // Delete button
        holder.deleteButton.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(topping));
    }

    @Override
    public int getItemCount() {
        return toppingList.size();
    }

    public static class ToppingViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        Button deleteButton;

        public ToppingViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.topping_name);
            price = itemView.findViewById(R.id.topping_price);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}

