package com.example.iventcalendar.entities.tabs.settings_tabs;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.iventcalendar.R;
import com.example.iventcalendar.services.interfaces.listeners.FragmentDataListener;
import com.example.iventcalendar.managers.PhotoFileManager;
import com.google.android.material.button.MaterialButton;

import java.io.File;

public class TabFirstPhotos extends Fragment implements FragmentDataListener {

    private static final String ARG_DATE = "date";

    private String date;

    private Uri photoURI;

    private ActivityResultLauncher<String> galleryLauncher;

    public static TabFirstPhotos newInstance(String arg) {
        TabFirstPhotos fragment = new TabFirstPhotos();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, arg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            date = getArguments().getString(ARG_DATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_photos_settings, container, false);
        TextView title = rootView.findViewById(R.id.photoTitle);
        title.setText(R.string.title_tab_photos_settings);
        ImageView photo = rootView.findViewById(R.id.image);
        MaterialButton changePhotoButton = rootView.findViewById(R.id.changePhotoButton);
        MaterialButton deletePhotoButton = rootView.findViewById(R.id.deletePhotoButton);

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                o -> {
                    if (o == null) return;
                    Glide.with(rootView).load(o).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(photo);
                    photo.setImageURI(o);
                    File savedImageFile = PhotoFileManager.saveImageByUri(
                            requireContext().getApplicationContext(),
                            o,
                            date + ".jpg");
                    photoURI = Uri.fromFile(savedImageFile);
                }
        );
        changePhotoButton.setOnClickListener(v -> galleryLauncher.launch("image/*"));

        deletePhotoButton.setOnClickListener(view -> {
            photo.setImageResource(0);
            photoURI = null;
            PhotoFileManager.deletePhotoFile(
                    requireContext().getApplicationContext(),
                    date + ".jpg");
        });

        return rootView;
    }

    @Override
    public String getFragmentData() {
        if (photoURI == null) return "";
        return this.photoURI.getPath();
    }
}
