package com.guo.news.util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtils {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT    = "yyyy-MM-dd";

    private DateUtils() {
    }


    public static String format(long timeInMilli, String formatStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
        return dateFormat.format(new Date(timeInMilli));
    }

}
