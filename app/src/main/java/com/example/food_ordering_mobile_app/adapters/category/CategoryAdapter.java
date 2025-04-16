package com.example.food_ordering_mobile_app.adapters.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.category.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> categoryList;
    private OnCategoryDeleteListener deleteListener;

    public interface OnCategoryDeleteListener {
        void onDeleteCategory(Category category, int position);
    }

    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public void setOnCategoryDeleteListener(OnCategoryDeleteListener listener) {
        this.deleteListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageButton deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            deleteButton = itemView.findViewById(R.id.btn_delete_category);
        }
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false); // Adjust name if necessary
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryName.setText(category.getName());

        holder.deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteCategory(category, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
