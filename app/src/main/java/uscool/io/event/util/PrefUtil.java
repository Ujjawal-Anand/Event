package uscool.io.event.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by andy1729 on 19/01/18.
 */

public class PrefUtil {
    private static final String USER_PREFERENCES = "user_preference";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "passowrd";
    private static final String IS_LOGGED_IN = "is_logged_in";



    public static void createUser(Context context,String username, String password) {
        putString(context, USERNAME, username);
        putString(context, PASSWORD, password);
        putBooleanData(context, IS_LOGGED_IN, true);
    }

    public static String getUsername(Context context) {
        return getString(context, USERNAME);
    }

    public static boolean isLoggedIn(Context context) {
        return getBooleanData(context, IS_LOGGED_IN, false);
    }

    public static void logOut(Context context) {
        putBooleanData(context, IS_LOGGED_IN, false);
    }

    private static void putString(Context context, final String NAME, final String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(NAME, value);
        editor.apply();
    }

    private static String getString(Context context, final String NAME) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(NAME, "null");
    }

    public static void putBooleanData(Context context, final String DATA_STRING_NAME, final boolean value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(DATA_STRING_NAME, value);
        editor.apply();
    }

    public static boolean getBooleanData(Context context, final String DATA_NAME, final boolean defValue) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(DATA_NAME, defValue);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.edit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
    }
}
