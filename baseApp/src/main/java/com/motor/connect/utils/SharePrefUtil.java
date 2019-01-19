package com.motor.connect.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefUtil {

    private Context context;
    private SharedPreferences prefs;

    public SharePrefUtil(Context mContext) {
        this.context = mContext;
        prefs = this.context.getSharedPreferences("com.motor.connect", Context.MODE_PRIVATE);
    }

    public void setFirstUserPref(String keyName, Boolean value) {
        prefs.edit().putBoolean(keyName, value).apply();

    }

    public Boolean getFirstUserPref(String keyName) {
        return prefs.getBoolean(keyName, true);
    }

    public void setTriggerData(String key, Boolean value) {
        prefs.edit().putBoolean(key, value).apply();

    }

    public Boolean getTriggerData(String key) {
        return prefs.getBoolean(key, false);
    }
}