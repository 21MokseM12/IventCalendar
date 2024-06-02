package com.example.iventcalendar.activities.tabs.settings_tabs;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.iventcalendar.R;
import com.example.iventcalendar.activities.EventSettingsActivity;
import com.example.iventcalendar.activities.tabs.settings_tabs.service.FragmentDataListener;
import com.google.android.material.button.MaterialButton;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class TabFirstPhotos extends Fragment implements FragmentDataListener {
    private Uri photoURI;
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
                        if (o == null) return;
                        Glide.with(rootView).load(o).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(photo);
                        photo.setImageURI(o);
                        saveTheImageByUriInApplicationStorage(o);
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
        return this.photoURI.getPath();
    }
    private void saveTheImageByUriInApplicationStorage(Uri imageUri) {
        Context context = getContext().getApplicationContext();
        try (InputStream input = context.getContentResolver().openInputStream(imageUri)) {
            if (input == null) return;

            File outputFile = new File(context.getFilesDir(), "profilePic.jpg");
            System.out.println(outputFile.toPath().toAbsolutePath().toString());
            try (OutputStream output = Files.newOutputStream(outputFile.toPath())) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            }
            photoURI = Uri.fromFile(outputFile);
            System.out.println(photoURI.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
