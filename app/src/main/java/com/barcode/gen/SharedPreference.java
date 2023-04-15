package com.barcode.gen;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreference {
    private static final String SHARED_PREFS = "sharedPrefs";

    public static boolean saveData(Context context,String TAG, String TEXT) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences.contains(TAG)){
            String sharedPrefString = sharedPreferences.getString(TAG, "");
            if(!(sharedPrefString.contains(TEXT))) {
                editor.putString(TAG, sharedPrefString + "," + TEXT);
            }
            else
            {
             return false;
            }
        }
        else
        {
            editor.putString(TAG, TEXT);
        }
        editor.apply();
        return true;
    }

    public static List<String> loadData(Context context, String TAG) {
        List<String> separated = new ArrayList<>();
        String text=null;
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if(sharedPreferences.contains(TAG)){
            text = sharedPreferences.getString(TAG, "");
        }
        if(text != null && text.contains(","))
        {
            separated.addAll(Arrays.asList(text.split(",")));
            return separated;
        }
        if(text != null && !text.contains(","))
        {
            separated.add(text);
            return separated;
        }
        else
        {
            return null;
        }
    }
}
