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

public class AvailableAndSelectedToppingAdapter extends RecyclerView.Adapter<AvailableAndSelectedToppingAdapter.ToppingViewHolder> {
    private List<Topping> availableToppings;
    private List<Topping> selectedToppings;

    public AvailableAndSelectedToppingAdapter(List<Topping> selectedToppings, List<Topping> availableToppings) {
        this.selectedToppings = selectedToppings != null ? selectedToppings : new ArrayList<>();
        this.availableToppings = availableToppings != null ? availableToppings : new ArrayList<>();
    }

    public void setAvailableToppings(List<Topping> toppings) {
        this.availableToppings = toppings != null ? toppings : new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<Topping> getSelectedToppings() {
        return selectedToppings;
    }

    @NonNull
    @Override
    public ToppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topping, parent, false);
        return new ToppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppingViewHolder holder, int position) {
        Topping topping = availableToppings.get(position);

        holder.tvToppingName.setText(topping.getName());
        boolean isSelected = isSelected(topping);
        holder.checkbox.setImageResource(isSelected ? R.drawable.ic_check_on : R.drawable.ic_check_off);
        holder.itemView.setBackgroundResource(
                isSelected ? R.drawable.bg_topping_selected : R.drawable.bg_topping_unselected
        );

        holder.itemView.setOnClickListener(v -> {
            if (isSelected(topping)) {
                removeToppingById(topping.getId());
            } else {
                selectedToppings.add(topping);
            }
            notifyItemChanged(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return availableToppings.size();
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

    private boolean isSelected(Topping topping) {
        for (Topping selected : selectedToppings) {
            if (selected.getId().equals(topping.getId())) {
                return true;
            }
        }
        return false;
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
