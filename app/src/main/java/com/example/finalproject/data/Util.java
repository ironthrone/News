package com.example.finalproject.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/9/25.
 */
public class Util {
    public static long getTimeStamp(String dateStr)  {
        DateFormat format = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date == null ? -1 : date.getTime();

    }
}
