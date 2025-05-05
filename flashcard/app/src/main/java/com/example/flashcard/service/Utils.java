package com.example.flashcard.service;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Utils {

    public static Uri frontImageUri;
    public static Uri frontSoundUri;
    public static Uri backImageUri;
    public static Uri backSoundUri;

    public static int PICK_IMAGE_FRONT =1;
    public static int PICK_AUDIO_FRONT =2;
    public static int PICK_IMAGE_BACK =3;
    public static int PICK_AUDIO_BACK =4;


    public static MultipartBody.Part getMultipartFromUri(Context context, Uri uri, String fieldName) {
        if (uri == null)
            return null;
        File file = getFileFromUri(context, uri);
        RequestBody requestBody = RequestBody.create(MediaType.parse(context.getContentResolver().getType(uri)), file);
        return MultipartBody.Part.createFormData(fieldName, file.getName(), requestBody);
    }
    public static String getFileName(Context context, Uri uri) {
        String fileName = null;
        try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public static String getFileExtension(Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        if (mimeType != null) {
            return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
        }
        return null;
    }

    private static File getFileFromUri(Context context, Uri uri) {
        if (uri==null)
            return new File("");
        File file = new File(context.getCacheDir(), getFileName(context,uri));
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new File("");
        }
        return file;
    }

}