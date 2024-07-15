package com.example.iventcalendar.managers;

import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private SharedPreferencesManager() {}

    public static void saveBoolean(SharedPreferences pref, String key, boolean value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void incrementIntByKey(SharedPreferences pref, String key) {
        pref.edit().putInt(key, pref.getInt(key, 0)+1).apply();
    }

    public static void decrementIntByKey(SharedPreferences pref, String key) {
        pref.edit().putInt(key, pref.getInt(key, 0)-1).apply();
    }

    public static void deleteByKey(SharedPreferences pref, String key) {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.apply();
    }

    public static boolean containsKey(SharedPreferences pref, String key) {
        return pref.contains(key);
    }
}
