package com.example.flashcard.data;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import java.util.Locale;

public class LocaleHelper {
    public static String getLanguagePreference(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        return prefs.getString("language", "en"); // Giá trị mặc định là "en"
    }

    public static void setAppLocale(Context context) {
        String languageCode = getLanguagePreference(context);
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);

        context.createConfigurationContext(config);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
    public static void saveLanguagePreference(Context context, String languageCode) {
        SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("language", languageCode);
        editor.apply();
    }

}