package com.lxf.tools.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPHelper {

    private static String DATA_NAME;

    private SharedPreferences mSharedPre;

    private static volatile SPHelper instance = null;

    private SPHelper() {
    }

    public static SPHelper getInstance() {
        if (instance == null) {
            synchronized (SPHelper.class) {
                if (instance == null) {
                    instance = new SPHelper();
                }
            }
        }
        return instance;
    }

    public void init(Context context, String name) {
        DATA_NAME = name;
        mSharedPre = context.getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE);
    }

    public Boolean getBoolean(String key, boolean def) {
        if (!isInit()) {
            throw new RuntimeException("please init SPHelper in application.");
        }
        return mSharedPre.getBoolean(key, def);
    }

    public void putBoolean(String key, boolean value) {
        if (!isInit()) {
            throw new RuntimeException("please init SPHelper in application.");
        }
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public int getInt(String key, int def) {
        if (!isInit()) {
            throw new RuntimeException("please init SPHelper in application.");
        }
        return mSharedPre.getInt(key, def);
    }

    public void putInt(String key, int value) {
        if (!isInit()) {
            throw new RuntimeException("please init SPHelper in application.");
        }
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putFloat(String key, float value) {
        if (!isInit()) {
            throw new RuntimeException("please init SPHelper in application.");
        }
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public float getFloat(String key, float def) {
        if (!isInit()) {
            throw new RuntimeException("please init SPHelper in application.");
        }
        return mSharedPre.getFloat(key, def);
    }

    public String getString(String key, String def) {
        if (!isInit()) {
            throw new RuntimeException("please init SPHelper in application.");
        }
        return mSharedPre.getString(key, def);
    }

    public void putString(String key, String value) {
        if (!isInit()) {
            throw new RuntimeException("please init SPHelper in application.");
        }
        SharedPreferences.Editor editor = mSharedPre.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private boolean isInit() {
        return mSharedPre != null && DATA_NAME != null;
    }
}
