package com.example.iventcalendar.managers;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class PhotoFileManager {

    private PhotoFileManager() {}

    public static void deletePhotoFile(Context context, String pathName) {
        File deletebaleFile = new File(context.getFilesDir(), pathName);
        deletebaleFile.delete();
    }

    public static File saveImageByUri(Context context, Uri imageUri, String pathName) {
        try (InputStream input = context.getContentResolver().openInputStream(imageUri)) {
            if (input == null) throw new Exception("File not found");

            File outputFile = new File(context.getFilesDir(), pathName);
            try (OutputStream output = Files.newOutputStream(outputFile.toPath())) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            }
            return outputFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getRenamedCopyOfPhotoFileUri(Context context, String date) {
        File file = new File(context.getFilesDir(), date + "_copy.jpg");
        file.renameTo(new File(context.getFilesDir(), date + ".jpg"));
        return context.getFilesDir().getPath() + "/" + date + ".jpg";
    }
}
