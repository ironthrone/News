package com.guo.news.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 *
 */
public class MeasureConverter {


    public static int px2dip(Context context, float pxValue) {
        float density = getDisplayMetrics(context).density;
        return ((int) (pxValue / density + 0.5f));

    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }


    public static int dip2px(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics) + 0.5f);
    }


    public static int px2sp(Context context, float pxValue) {
        final float fontScale = getDisplayMetrics(context).scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    public static int sp2px(Context context, float spValue) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getDisplayMetrics(context)) + 0.5f);
    }

}
