package com.example.food_ordering_mobile_app.adapters;

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

public class ToppingDetailAdapter extends RecyclerView.Adapter<ToppingDetailAdapter.ViewHolder>{
    private final List<Topping> toppingList;

    public ToppingDetailAdapter(List<Topping> toppingList) {
        this.toppingList = toppingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topping_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppingDetailAdapter.ViewHolder holder, int position) {
        Topping topping = toppingList.get(position);

        holder.toppingName.setText(topping.getName() != null ? topping.getName() : "Không xác định");
        holder.toppingPrice.setText(topping.getPrice() >= 0 ? String.valueOf(topping.getPrice()) : "N/A");

    }


    @Override
    public int getItemCount() {
        return toppingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView toppingName, toppingPrice;
        Button modButton;
        public ViewHolder(View itemView) {
            super(itemView);
            toppingName = itemView.findViewById(R.id.topping_name);
            toppingPrice = itemView.findViewById(R.id.topping_price);
            modButton = itemView.findViewById(R.id.mod_button);
        }
    }
}
