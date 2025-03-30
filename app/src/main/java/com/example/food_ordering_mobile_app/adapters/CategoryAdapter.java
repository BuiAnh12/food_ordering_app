package com.example.food_ordering_mobile_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.category.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    private List<Category> categoryList;

    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category category = categoryList.get(position);

        holder.categoryName.setText(category.getName() != null ? category.getName() : "Không xác định");
    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            cardView = itemView.findViewById(R.id.item_topping);
        }
    }
}
