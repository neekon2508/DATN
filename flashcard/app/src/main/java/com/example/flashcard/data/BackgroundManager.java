package com.example.flashcard.data;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.example.flashcard.R;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileOutputStream;

public abstract class BackgroundManager {
    public static void setDefaultBackground(Context context, NavigationView navigationView) {
        String backgroundPath="";
        ImageView background = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.background);
        File file = new File(context.getFilesDir(), "flashcard.png");
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                background.setImageBitmap(bitmap);
            }
            else {
                background.setImageResource(R.drawable.flashcard);
            }
        }
    public static void saveImageToInternalStorage(Context context, Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            File file = new File(context.getFilesDir(), "flashcard.png");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            System.out.println("Image saved at: "+file.getAbsolutePath());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
