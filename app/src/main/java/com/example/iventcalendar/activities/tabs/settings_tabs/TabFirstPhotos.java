package com.example.iventcalendar.activities.tabs.settings_tabs;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.iventcalendar.R;
import com.example.iventcalendar.activities.tabs.settings_tabs.service.FragmentDataListener;
import com.google.android.material.button.MaterialButton;

public class TabFirstPhotos extends Fragment implements FragmentDataListener {
    private String photoURI;
    private ActivityResultLauncher<String> galleryLauncher;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_photos_settings, container, false);
        TextView title = rootView.findViewById(R.id.photoTitle);
        title.setText("Выбери фото, которое лучше всего описывает иВеНт)");
        ImageView photo = rootView.findViewById(R.id.image);
        MaterialButton changePhotoButton = rootView.findViewById(R.id.changePhotoButton);

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        try {
                            Glide.with(rootView).load(o).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(photo);
                            photo.setImageURI(o);
                            photoURI = o.toString();
                            System.out.println(photoURI);
                            System.out.println(o);
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

    @Override
    public String getFragmentData() {
        return this.photoURI;
    }
}
