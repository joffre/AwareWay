package com.pji.de.awareway.utilitaires;

import android.content.SharedPreferences;

import com.pji.de.awareway.MainActivity;

/**
 * Created by Geoffrey on 01/05/2016.
 */
public class AwarePreferences {

    private static SharedPreferences preferences;

    public static final String GOOGLE_AUTHENTIFIED = "log.authentified.google";

    public static void init(MainActivity mainActivity){
        preferences = mainActivity.getPreferences(mainActivity.MODE_PRIVATE);
    }

    public static String getStringPreference(String id){
        return preferences.getString(id, "");
    }

    public static void setStringPreference(String id, String value){
        preferences.edit().putString(id, value).commit();
    }

    public static boolean getBooleanPreference(String id){
        return preferences.getBoolean(id, false);
    }

    public static void setBooleanPreference(String id, boolean value){
        preferences.edit().putBoolean(id, value).commit();
    }
}
