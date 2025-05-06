package com.example.food_ordering_mobile_app.ui.store;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.bumptech.glide.Glide;
import com.example.food_ordering_mobile_app.R;
import com.example.food_ordering_mobile_app.models.image.Image;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.viewModel.StoreDetailViewModel;
import com.example.food_ordering_mobile_app.viewModel.UploadViewModel;

import java.util.ArrayList;
import java.util.List;

public class StoreDetailFragment extends Fragment {

    private StoreDetailViewModel storeDetailViewModel;
    private UploadViewModel uploadViewModel;
    Button changeCoverButton, changeAvatarButton;
    private Store store;

    private static final int REQUEST_AVATAR_IMAGE = 1;
    private static final int REQUEST_COVER_IMAGE = 2;

    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 100;
    private Uri selectedImageUri;
    private boolean isUpdatingAvatar = false;


    public StoreDetailFragment() {
        // Required empty public constructor
    }

    public static StoreDetailFragment newInstance() {
        StoreDetailFragment fragment = new StoreDetailFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    private void checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requireContext().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
            }
        } else {
            if (requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_detail, container, false);

        changeCoverButton = view.findViewById(R.id.changeCoverBtn);
        changeAvatarButton = view.findViewById(R.id.changeAvatarBtn);

        uploadViewModel = new ViewModelProvider(this).get(UploadViewModel.class);

        changeAvatarButton.setOnClickListener(v -> {
            isUpdatingAvatar = true;
            checkAndRequestPermission();
            openImagePicker(REQUEST_AVATAR_IMAGE);
        });

        changeCoverButton.setOnClickListener(v -> {
            isUpdatingAvatar = false;
            checkAndRequestPermission();
            openImagePicker(REQUEST_COVER_IMAGE);
        });
        storeDetailViewModel = new ViewModelProvider(this).get(StoreDetailViewModel.class);
        storeDetailViewModel.getStore();
        storeDetailViewModel.getStoreResponse().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    store = resource.getData();
                    loadData(store);
                    break;
                    case ERROR:
                        break;
            }
        });
        storeDetailViewModel.getSetStoreResponse().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    // show loading spinner if needed
                    break;
                case SUCCESS:
                    Toast.makeText(requireContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    Toast.makeText(requireContext(), "Cập nhật thất bại: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        TextView backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });


        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Người dùng đã cấp quyền
                openImagePicker(isUpdatingAvatar ? REQUEST_AVATAR_IMAGE : REQUEST_COVER_IMAGE);
            } else {
                Toast.makeText(getContext(), "Bạn cần cấp quyền để chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void openImagePicker(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    private void loadData(Store store) {
        TextView storeName = getView().findViewById(R.id.storeName);
        TextView description = getView().findViewById(R.id.description);
        TextView address = getView().findViewById(R.id.address);

        ImageView editStoreName = getView().findViewById(R.id.editStoreNameBtn);
        ImageView editDescription = getView().findViewById(R.id.editDescriptionBtn);
        ImageView editAddress = getView().findViewById(R.id.editAddressBtn);

        EditableField editableStoreName = new EditableField(storeName, editStoreName, "name", () -> {
            store.setName(storeName.getText().toString());
            storeDetailViewModel.setStore(store);
        });

        EditableField editableDescription = new EditableField(description, editDescription, "description", () -> {
            store.setDescription(description.getText().toString());
            storeDetailViewModel.setStore(store);
        });

        EditableField editableAddress = new EditableField(address, editAddress, "address", () -> {
            store.getAddress().setFullAddress(address.getText().toString());
            storeDetailViewModel.setStore(store);
        });


        editStoreName.setOnClickListener(v -> editableStoreName.toggle());
        editDescription.setOnClickListener(v -> editableDescription.toggle());
        editAddress.setOnClickListener(v -> editableAddress.toggle());

        editableStoreName.disableEditingOnFocusLost();
        editableDescription.disableEditingOnFocusLost();
        editableAddress.disableEditingOnFocusLost();

        TextView rating = getView().findViewById(R.id.rating);
        TextView rateAmount = getView().findViewById(R.id.rateAmount);
        ImageView avatar = getView().findViewById(R.id.avatarImage);
        ImageView coverImage = getView().findViewById(R.id.coverImage);

        storeName.setText(store.getName());
        description.setText(store.getDescription());
        Log.d("StoreDetail", "Address: " + store.getAddress().getFullAddress());
        address.setText(store.getAddress().getFullAddress());
        rating.setText(store.getAvgRating() + " ⭐");
        rateAmount.setText(store.getAmountRating());

        Log.d("StoreDetail", "Avatar URL: " + store.getAvatar().getUrl());
        Log.d("StoreDetail", "Cover URL: " + store.getCover().getUrl());

        Glide.with(avatar.getContext()).load(store.getAvatar().getUrl()).into(avatar);
        Glide.with(coverImage.getContext()).load(store.getCover().getUrl()).into(coverImage);


    }

    private class EditableField {
        TextView field;
        ImageView editButton;
        String originalValue;
        boolean isEditing = false;
        final String fieldName; // name to update in Store
        final Runnable onSave;

        EditableField(TextView field, ImageView editButton, String fieldName, Runnable onSave) {
            this.field = field;
            this.editButton = editButton;
            this.fieldName = fieldName;
            this.onSave = onSave;

            this.originalValue = field.getText().toString();
            field.setFocusable(false);
            field.setFocusableInTouchMode(false);
            field.setCursorVisible(false);
        }

        void toggle() {
            if (!isEditing) {
                isEditing = true;
                originalValue = field.getText().toString();
                field.setFocusableInTouchMode(true);
                field.setFocusable(true);
                field.setCursorVisible(true);
                field.requestFocus();
            } else {
                isEditing = false;
                field.setFocusable(false);
                field.setFocusableInTouchMode(false);
                field.setCursorVisible(false);

                String currentValue = field.getText().toString();
                if (!currentValue.equals(originalValue)) {
                    onSave.run(); // Trigger update if changed
                } else {
                    field.setText(originalValue); // Revert if no change
                }
            }
        }

        void disableEditingOnFocusLost() {
            field.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus && isEditing) {
                    isEditing = false;
                    String currentValue = field.getText().toString();
                    if (!currentValue.equals(originalValue)) {
                        onSave.run();
                    } else {
                        field.setText(originalValue); // Revert if no change
                    }

                    field.setFocusable(false);
                    field.setFocusableInTouchMode(false);
                    field.setCursorVisible(false);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            if (requestCode == REQUEST_AVATAR_IMAGE) {
                ImageView avatar = getView().findViewById(R.id.avatarImage);
                Glide.with(this).load(selectedImageUri).into(avatar);
                uploadImageAndUpdateStore(selectedImageUri, true);
            } else if (requestCode == REQUEST_COVER_IMAGE) {
                ImageView cover = getView().findViewById(R.id.coverImage);
                Glide.with(this).load(selectedImageUri).into(cover);
                uploadImageAndUpdateStore(selectedImageUri, false);
            }
        }
    }



    private void uploadImageAndUpdateStore(Uri imageUri, boolean isAvatar) {
        List<Uri> imageUris = new ArrayList<>();
        imageUris.add(selectedImageUri);
        uploadViewModel.uploadImages(imageUris, getContext());
        uploadViewModel.getUploadImagesResponse().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case SUCCESS:
                    List<Image> images = resource.getData();
                    Image uploadedImage = images.get(0);
                    if (isAvatar) {
                        store.setAvatar(uploadedImage); // hoặc store.getAvatar().setUrl(...)
                    } else {
                        store.setCover(uploadedImage);
                    }
                    storeDetailViewModel.setStore(store);
                    break;

                case ERROR:
                    Toast.makeText(getContext(), "Upload ảnh thất bại: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }




}