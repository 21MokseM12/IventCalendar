package com.example.iventcalendar.activities.tabs.info_tabs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.iventcalendar.R;
import com.example.iventcalendar.activities.tabs.settings_tabs.service.FragmentDataListener;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class TabFirstPhotos  extends Fragment implements FragmentDataListener {
    private static final String ARG_PHOTO_URI = "photoURI";
    private static final String ARG_DATE = "date";
    private String date;
    private Uri photoURI;
    private ActivityResultLauncher<String> galleryLauncher;
    private ImageView photo;
    private View rootView;

    public static TabFirstPhotos newInstance(String uri, String date) {
        TabFirstPhotos fragment = new TabFirstPhotos();
        Bundle args = new Bundle();
        args.putString(ARG_PHOTO_URI, uri);
        args.putString(ARG_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            photoURI = Uri.parse(getArguments().getString(ARG_PHOTO_URI));
            date = getArguments().getString(ARG_DATE);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab1_photos_settings, container, false);

        TextView title = rootView.findViewById(R.id.photoTitle);
        title.setText("Весь день в одной фотографии:");
        photo = rootView.findViewById(R.id.image);
        MaterialButton changePhotoButton = rootView.findViewById(R.id.changePhotoButton);

        if (!photoURI.equals(Uri.parse(""))) {
            Glide.with(rootView).load(new File(requireActivity().getApplicationContext().getFilesDir() + "/" + date + ".jpg"))
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(photo);
        } else Toast.makeText(requireContext(), "Фото не было выбрано(", Toast.LENGTH_LONG).show();

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        try {
                            if (o == null) return;
                            Glide.with(rootView)
                                    .load(o)
                                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                                    .into(photo);
                            photo.setImageURI(o);
                            saveTheImageByUriInApplicationStorage(o);
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
    private void saveTheImageByUriInApplicationStorage(Uri imageUri) {
        Context context = requireContext().getApplicationContext();
        try (InputStream input = context.getContentResolver().openInputStream(imageUri)) {
            if (input == null) return;

            File outputFile = new File(context.getFilesDir(), date + "_copy.jpg");
            try (OutputStream output = Files.newOutputStream(outputFile.toPath())) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            }
            photoURI = Uri.fromFile(outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}