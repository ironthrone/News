package com.guo.news.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.Preference;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016/11/26.
 */
public class DownloadPreference extends Preference {
    public DownloadPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onClick() {
        super.onClick();
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm to update")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
}
