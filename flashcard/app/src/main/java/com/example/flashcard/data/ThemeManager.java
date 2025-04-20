package com.example.flashcard.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class ThemeManager {

    public static void setTheme(Context context) {

        String theme = getTheme(context);
        int themeResourceId = context.getResources().getIdentifier(theme, "style", context.getPackageName());
        context.setTheme(themeResourceId);

    }
    public static String getTheme(Context context) {
        String theme =null;
         try (FlashCardSQLiteHelper flashCardSQLiteHelper = new FlashCardSQLiteHelper(context);
        SQLiteDatabase db = flashCardSQLiteHelper.getReadableDatabase();
        Cursor themeCursor = db.query("SETTING", new String[] {"THEME"},
                null,null, null, null, null);)
        {
            if(themeCursor.moveToFirst()) {
                theme = themeCursor.getString(0);
            }
        }
        return theme;
    }
    public static void updateTheme(Context context, String newTheme) {
        try (FlashCardSQLiteHelper flashCardSQLiteHelper = new FlashCardSQLiteHelper(context);
             SQLiteDatabase db = flashCardSQLiteHelper.getReadableDatabase()) {
            FlashCardSQLiteHelper.updateTheme(db, newTheme);
        }
    }

}
