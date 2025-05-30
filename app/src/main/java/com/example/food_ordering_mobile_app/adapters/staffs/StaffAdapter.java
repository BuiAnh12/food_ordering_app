package com.example.food_ordering_mobile_app.adapters.staffs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.user.User;

import java.util.ArrayList;
import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {

    private List<User> originalList;   // Full list
    private List<User> filteredList;   // Filtered by search
    private OnStaffClickListener listener;

    public StaffAdapter(List<User> staffList, OnStaffClickListener listener) {
        this.originalList = staffList != null ? staffList : new ArrayList<>();
        this.filteredList = new ArrayList<>(this.originalList);
        this.listener = listener;
    }

    public interface OnStaffClickListener {
        void onStaffClick(User staff);
    }

    public void filter(String keyword) {
        filteredList.clear();
        if (keyword == null || keyword.trim().isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            String lowerKeyword = keyword.toLowerCase();
            for (User staff : originalList) {
                if (staff.getName().toLowerCase().contains(lowerKeyword)
                        || staff.getTopRole().toLowerCase().contains(lowerKeyword)) {
                    filteredList.add(staff);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void updateData(List<User> newStaffList) {
        this.originalList = newStaffList != null ? newStaffList : new ArrayList<>();
        this.filteredList = new ArrayList<>(this.originalList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        User staff = filteredList.get(position);
        holder.bind(staff);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    class StaffViewHolder extends RecyclerView.ViewHolder {
        private TextView textName, textRole, textStatus;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_staff_name);
            textRole = itemView.findViewById(R.id.text_staff_role);
            textStatus = itemView.findViewById(R.id.text_status_badge);
        }

        public void bind(final User staff) {
            textName.setText(staff.getName());
            textRole.setText(staff.getTopRole().contains("owner") ? "Chủ cửa hàng" : ( staff.getTopRole().contains("manager") ? "Quản lý" : "Nhân viên"));
            textStatus.setText("Hoạt động");

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onStaffClick(staff);
                }
            });
        }
    }
}