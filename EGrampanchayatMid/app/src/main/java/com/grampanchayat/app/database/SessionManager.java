package com.grampanchayat.app.database;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "GrampanchayatPref";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_TYPE = "userType";
    private static final String KEY_DARK_MODE = "darkMode";
    private static final String KEY_LANGUAGE = "language";

    public static final String USER_TYPE_ADMIN = "admin";
    public static final String USER_TYPE_USER = "user";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createSession(String userType) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_TYPE, userType);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUserType() {
        return pref.getString(KEY_USER_TYPE, "user");
    }

    public boolean isAdmin() {
        return USER_TYPE_ADMIN.equals(getUserType());
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

    public void setDarkMode(boolean isDark) {
        editor.putBoolean(KEY_DARK_MODE, isDark);
        editor.apply();
    }

    public boolean isDarkMode() {
        return pref.getBoolean(KEY_DARK_MODE, false);
    }

    public void setLanguage(String lang) {
        editor.putString(KEY_LANGUAGE, lang);
        editor.apply();
    }

    public String getLanguage() {
        return pref.getString(KEY_LANGUAGE, "english");
    }

    public boolean isMarathi() {
        return "marathi".equals(getLanguage());
    }
}
