package com.example.iventcalendar.activities.tabs.info_tabs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.iventcalendar.R;
import com.example.iventcalendar.activities.EventInfoActivity;
import com.example.iventcalendar.activities.tabs.settings_tabs.service.FragmentDataListener;
import com.google.android.material.button.MaterialButton;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

public class TabFirstPhotos  extends Fragment implements FragmentDataListener {
    private static final String ARG_PHOTO_URI = "photoURI";
    private Uri photoURI;
    private ActivityResultLauncher<String> galleryLauncher;
    private ImageView photo;

    public static TabFirstPhotos newInstance(String arg) {
        TabFirstPhotos fragment = new TabFirstPhotos();
        Bundle args = new Bundle();
        args.putString(ARG_PHOTO_URI, arg);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            photoURI = Uri.parse(getArguments().getString(ARG_PHOTO_URI));
            System.out.println(photoURI.getPath());
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_photos_settings, container, false);

        TextView title = rootView.findViewById(R.id.photoTitle);
        title.setText("Весь день в одной фотографии:");
        photo = rootView.findViewById(R.id.image);
        MaterialButton changePhotoButton = rootView.findViewById(R.id.changePhotoButton);

        if (photoURI != null) {
//            Glide.with(rootView).load(photoURI).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(photo);
            photo.setImageURI(photoURI);
        } else Toast.makeText(requireContext(), "Фото не было выбрано(", Toast.LENGTH_LONG).show();

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        try {
                            Glide.with(rootView).load(o).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(photo);
                            photo.setImageURI(o);
                            photoURI = o;
//                            System.out.println(photoURI);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        changePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryLauncher.launch("image/*");
            }
        });
        return rootView;
    }
    public String getFragmentData() {return this.photoURI.getPath();}
}

