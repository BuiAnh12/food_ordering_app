package com.example.food_ordering_mobile_app.adapters.dish;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.dish.Dish;
import com.example.food_ordering_mobile_app.models.image.Image;
import com.example.food_ordering_mobile_app.ui.menu.FragmentDishDetail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

public class InnerDishAdapter extends RecyclerView.Adapter<InnerDishAdapter.ViewHolder> {
    private List<Dish> dishList;

    public InnerDishAdapter(List<Dish> dishList) {
        this.dishList = dishList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_dish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerDishAdapter.ViewHolder holder, int position) {
        Dish dish = dishList.get(position);

        holder.dish_name.setText(dish.getName() != null ? dish.getName() : "Không xác định");
        holder.dish_price.setText(dish.getPrice() > 0 ? String.valueOf(dish.getPrice()) : "N/A");
        Image dish_image = dish.getImage();

        Glide.with(holder.itemView.getContext())
                .asBitmap()
                .load(dish_image.getUrl() != null ? dish_image.getUrl() : R.drawable.menu_1)
                .apply(new RequestOptions().override(110, 110).centerCrop())
                .placeholder(R.drawable.menu_1)
                .error(R.drawable.menu_1)
                .into(new BitmapImageViewTarget(holder.dish_image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedDrawable =
                                RoundedBitmapDrawableFactory.create(holder.itemView.getContext().getResources(), resource);
                        roundedDrawable.setCornerRadius(999);
                        holder.dish_image.setImageDrawable(roundedDrawable);
                    }
                });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of the fragment with arguments
                FragmentDishDetail fragment = FragmentDishDetail.newInstance(dish.getId());

                // Get FragmentManager (make sure this code runs inside an Activity or Fragment)
                FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();

                // Begin transaction and replace the container
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_store_main, fragment)  // Make sure R.id.fragment_container exists in your layout
                        .addToBackStack(null)  // Adds to back stack so user can go back
                        .commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView  dish_image;

        TextView dish_name, dish_price;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            dish_image = itemView.findViewById(R.id.dish_image);
            dish_name = itemView.findViewById(R.id.dish_name);
            dish_price = itemView.findViewById(R.id.dish_price);
            cardView = itemView.findViewById(R.id.item_topping);
//            enable_toggle = itemView.findViewById(R.id.dish_toggle);
        }
    }
}
