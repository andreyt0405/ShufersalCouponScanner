package com.barcode.gen;

import android.util.Log;
import android.widget.NumberPicker;

import static android.content.ContentValues.TAG;

public class StringParse {
    public static String stringParseByRegex(String text, String Regex)
    {
        String[] items = text.split("\n");
        for(int i=0; i<items.length; i++)
        {
            if(items[i].matches(Regex))
            {
                Log.d(TAG, "STRING HAS FOUND"+items[i]);
                return items[i];
            }
        }
        return "Not found";
    }
}

