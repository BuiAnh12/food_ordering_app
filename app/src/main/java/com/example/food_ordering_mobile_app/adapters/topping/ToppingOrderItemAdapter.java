package com.example.food_ordering_mobile_app.adapters.topping;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.topping.Topping;

import java.util.ArrayList;
import java.util.List;

public class ToppingOrderItemAdapter extends RecyclerView.Adapter<ToppingOrderItemAdapter.ToppingViewHolder>{
    private List<Topping> selectedToppings;

    public ToppingOrderItemAdapter(List<Topping> selectedToppings) {
        this.selectedToppings = selectedToppings != null ? selectedToppings : new ArrayList<>();
    }

    public List<Topping> getSelectedToppings() {
        return selectedToppings;
    }

    @NonNull
    @Override
    public ToppingOrderItemAdapter.ToppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topping, parent, false);
        return new ToppingOrderItemAdapter.ToppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppingOrderItemAdapter.ToppingViewHolder holder, int position) {
        Topping topping = selectedToppings.get(position);

        holder.tvToppingName.setText(topping.getName());
        holder.checkbox.setImageResource(R.drawable.ic_check_on);
        holder.itemView.setBackgroundResource(R.drawable.bg_topping_selected);

        holder.itemView.setOnClickListener(v -> {
            removeToppingById(topping.getId());
            notifyItemChanged(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return selectedToppings.size();
    }

    static class ToppingViewHolder extends RecyclerView.ViewHolder {
        TextView tvToppingName;
        ImageView checkbox;

        public ToppingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvToppingName = itemView.findViewById(R.id.tv_topping_name);
            checkbox = itemView.findViewById(R.id.img_check);
        }
    }


    private void removeToppingById(String id) {
        for (int i = 0; i < selectedToppings.size(); i++) {
            if (selectedToppings.get(i).getId().equals(id)) {
                selectedToppings.remove(i);
                return;
            }
        }
    }
}
