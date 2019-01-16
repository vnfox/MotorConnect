package com.motor.connect.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {

    public static final int PERMISSION_REQUEST_CODE = 10;

    private static final String PREFS_FILE_NAME = "preference_permission";
    private static final String PREFS_FIRST_TIME_KEY = "is_app_launched_first_time";

    private PermissionUtils() {

    }

    // preference utility methods
    public static boolean isFistLaunch(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREFS_FIRST_TIME_KEY, true);
    }

    public static void setLaunchedFirstTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFS_FIRST_TIME_KEY, false);
        editor.apply();
    }

    public static boolean isRuntimePermissionRequired() {
        return (Build.VERSION.SDK_INT >= 23);
    }

    public static boolean isGranted(Context context, String permissionName) {
        return ActivityCompat
                .checkSelfPermission(context, permissionName) ==
                PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isPermissionsGranted(Activity activity, String[] permissions, int requestCode) {

        List<String> requestPermission = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity,
                    permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermission.add(permission);
            }
        }

        if (!requestPermission.isEmpty()) {
            ActivityCompat.requestPermissions(activity, requestPermission.toArray(
                    new String[requestPermission.size()]), requestCode);
            return false;
        }

        return true;
    }

    public static boolean isDeviceIdNeedPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }
}
