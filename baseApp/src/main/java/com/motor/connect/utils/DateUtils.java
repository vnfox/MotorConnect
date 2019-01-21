package com.motor.connect.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static final String DD_MMM_YYYY = "dd MMM yyyy";
    public static final String HH_MM = "HH:mm";

    public static String getCurrentDate(String pattern) {
        return getFormatDate(System.currentTimeMillis(), pattern);
    }

    public static String getFormatDate(long milliseconds, String format) {
        if (milliseconds != 0L) {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
            return simpleDateFormat.format(new Date(milliseconds));
        }
        return null;
    }

    public static String getTime(long milliseconds, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        return format.format(new Date(milliseconds));
    }

}