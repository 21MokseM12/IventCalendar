package com.example.iventcalendar.managers;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

public class PhotoFormatManager {

    private PhotoFormatManager() {}

    public static void formatPhotoByUri(View rootView, Uri uri, ImageView photoView) {
        Glide.with(rootView)
                .load(uri)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                .into(photoView);
    }

    public static void formatPhotoByFile(View rootView, File file, ImageView photoView) {
        Glide.with(rootView).load(file)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(photoView);
    }
}
