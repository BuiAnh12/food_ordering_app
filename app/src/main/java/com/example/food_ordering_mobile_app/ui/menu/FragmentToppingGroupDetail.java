package com.example.food_ordering_mobile_app.ui.menu;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.adapters.topping.ToppingDetailAdapter;
import com.example.food_ordering_mobile_app.models.topping.Topping;
import com.example.food_ordering_mobile_app.models.topping.ToppingGroup;
import com.example.food_ordering_mobile_app.repository.ToppingRepository;
import com.example.food_ordering_mobile_app.viewModel.ToppingViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentToppingGroupDetail extends Fragment {

    private RecyclerView recyclerView;
    private ToppingDetailAdapter adapter;
    private List<Topping> toppingList;
    private ToppingRepository toppingRepository;

    private ToppingViewModel toppingViewModel;

    private String toppingGroupId;



    public FragmentToppingGroupDetail() {
        // Required empty public constructor
    }

    public static FragmentToppingGroupDetail newInstance(String id) {
        FragmentToppingGroupDetail fragment = new FragmentToppingGroupDetail();
        Bundle args = new Bundle();
        args.putString("TOPPING_GROUP_ID", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            toppingGroupId = getArguments().getString("TOPPING_GROUP_ID");
        }
        toppingRepository = new ToppingRepository(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topping_group_detail, container, false);
        toppingList = new ArrayList<>();
        adapter = new ToppingDetailAdapter(toppingList,
                topping -> showEditDialog(topping),
                topping -> deleteTopping(topping));
        TextView deleteGroupBtn = view.findViewById(R.id.deleteGroup);
        deleteGroupBtn.setOnClickListener(v -> confirmDeleteGroup());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        TextView addDishBtn = view.findViewById(R.id.addDish);
        addDishBtn.setOnClickListener(v -> showAddDialog());
        toppingViewModel = new ViewModelProvider(this).get(ToppingViewModel.class);

        toppingViewModel.getToppingResponse().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    // Optionally show a loading spinner
                    break;
                case SUCCESS:
                    if (resource.getData() != null) {
                        showToppings(resource.getData());
                    } else {
                        Toast.makeText(requireContext(), "Lỗi: không có dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ERROR:
                    Toast.makeText(requireContext(), "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        toppingViewModel.updateToppingResponse().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case SUCCESS:
                    Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    toppingViewModel.getTopping(toppingGroupId); // reload
                    break;
                case ERROR:
                    Toast.makeText(getContext(), "Lỗi cập nhật: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case LOADING:
                    // Optional: show loading
                    break;
            }
        });

        toppingViewModel.addToppingToGroupResponse().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case SUCCESS:
                    Toast.makeText(getContext(), "Thêm topping thành công", Toast.LENGTH_SHORT).show();
                    toppingViewModel.getTopping(toppingGroupId); // refresh list
                    break;
                case ERROR:
                    Toast.makeText(getContext(), "Lỗi thêm topping: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case LOADING:
                    // Optionally show loading
                    break;
            }
        });

        toppingViewModel.deleteToppingGroupResponse().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case SUCCESS:
                    Toast.makeText(getContext(), "Xóa nhóm topping thành công", Toast.LENGTH_SHORT).show();
                    goBack(null); // Go back to previous screen
                    break;
                case ERROR:
                    Toast.makeText(getContext(), "Lỗi xóa nhóm: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case LOADING:
                    // Optional: show loading
                    break;
            }
        });

        toppingViewModel.removeToppingFromGroupResponse().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case SUCCESS:
                    Toast.makeText(getContext(), "Xóa topping thành công", Toast.LENGTH_SHORT).show();
                    toppingViewModel.getTopping(toppingGroupId); // refresh list
                    break;
                case ERROR:
                    Toast.makeText(getContext(), "Lỗi thêm topping: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case LOADING:
                    // Optionally show loading
                    break;
            }
        });



        toppingViewModel.getTopping(toppingGroupId);

        TextView goBackBtn = view.findViewById(R.id.backBtn);
        goBackBtn.setOnClickListener(this::goBack);

        return view;
    }

    private void showToppings(ToppingGroup toppingGroup) {
        toppingList.clear();
        toppingList.addAll(toppingGroup.getToppings());
        adapter.notifyDataSetChanged();
    }

    public void goBack(View view) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void showAddDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_topping, null);
        EditText nameInput = dialogView.findViewById(R.id.edit_topping_name);
        EditText priceInput = dialogView.findViewById(R.id.edit_topping_price);

        new AlertDialog.Builder(requireContext())
                .setTitle("Thêm Topping")
                .setView(dialogView)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String priceStr = priceInput.getText().toString().trim();

                    if (name.isEmpty() || priceStr.isEmpty()) {
                        Toast.makeText(requireContext(), "Tên và giá topping không được để trống", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        double price = Double.parseDouble(priceStr);
                        Topping newTopping = new Topping(null, name, price); // ID null or empty if server generates it
                        toppingViewModel.addToppingToGroup(toppingGroupId, newTopping);
                    } catch (NumberFormatException e) {
                        Toast.makeText(requireContext(), "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showEditDialog(Topping topping) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_topping, null);
        EditText nameInput = dialogView.findViewById(R.id.edit_topping_name);
        EditText priceInput = dialogView.findViewById(R.id.edit_topping_price);

        nameInput.setText(topping.getName());
        priceInput.setText(String.valueOf(topping.getPrice()));

        new AlertDialog.Builder(requireContext())
                .setTitle("Sửa Topping")
                .setView(dialogView)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    String newName = nameInput.getText().toString().trim();
                    double newPrice = Double.parseDouble(priceInput.getText().toString().trim());

                    Topping updated = new Topping(topping.getId(), newName, newPrice);
                    toppingViewModel.updateTopping(toppingGroupId, topping.getId(), updated);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteTopping(Topping topping) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa topping này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    toppingViewModel.removeToppingFromGroup(toppingGroupId, topping.getId());
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void confirmDeleteGroup() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa nhóm")
                .setMessage("Bạn có chắc chắn muốn xóa toàn bộ nhóm topping này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    toppingViewModel.deleteToppingGroup(toppingGroupId);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }


}