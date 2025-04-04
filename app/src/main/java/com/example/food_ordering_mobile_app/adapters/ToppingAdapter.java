package com.example.food_ordering_mobile_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.topping.ToppingGroup;
import com.example.food_ordering_mobile_app.ui.menu.FragmentToppingGroupDetail;

import java.util.List;

public class ToppingAdapter extends RecyclerView.Adapter<ToppingAdapter.ViewHolder>{
    private List<ToppingGroup> toppingGroups;

    public ToppingAdapter(List<ToppingGroup> toppingGroups) {
        this.toppingGroups = toppingGroups;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_topping, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppingAdapter.ViewHolder holder, int position) {
        ToppingGroup toppingGroup = toppingGroups.get(position);

        holder.groupName.setText(toppingGroup.getName() != null ? toppingGroup.getName() : "Không xác định");
        holder.toppingNumber.setText(!toppingGroup.getToppings().isEmpty() ? String.valueOf(toppingGroup.getToppings().size()) : "N/A");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a new instance of the fragment with arguments
                FragmentToppingGroupDetail fragment = FragmentToppingGroupDetail.newInstance();

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
        return toppingGroups.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView groupName, toppingNumber;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.group_name);
            toppingNumber = itemView.findViewById(R.id.topping_number);
            cardView = itemView.findViewById(R.id.item_topping);
        }
    }
}

