package com.example.finalproject.data;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by Administrator on 2016/9/25.
 */
public class TheLoader extends AsyncTaskLoader<Cursor> {
    private String mChannelId;
    private int mPage;
    private long mLastNewsTime;


    public TheLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
//        if(mLastNewsTime)
        return null;
    }
}
