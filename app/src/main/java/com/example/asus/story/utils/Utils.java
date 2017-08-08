package com.example.asus.story.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by asus on 8/5/2017.
 */

public class Utils {

    public static final String BASE_URL = "http://kookyapps.com/smv/api/";
    public static final String CATEGORY_URL = "newsType";
    public static final String PROFILE_URL = "profile";
    public static final String FETCH_NEWS_URL = "FetchNews";
    public static final String UPLOAD_NEWS_URL = "uploadNews";
    public static final String VERIFY_OTP_URL = "verifyOtp";
    public static final String FTP_IMG_URL = "http://kookyapps.com/smv/uploads/";
    public static final String FB_LOGIN_URL = "fblogin";

    public static String userId="userId";
    public static String userName="userName";
    public static String userFbId="userFbId";
    public static String userEmail="userEmail";
    public static String userImg="userImg";

    public static void setShared(Context context, String name, String value) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static String getShared(Context context, String name, String defaultValue) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return prefs.getString(name, defaultValue);

    }

    public static void ClearShared(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

}
