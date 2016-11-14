package com.guo.news.data;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

import com.guo.news.util.NetworkChecker;

/**
 * Created by Administrator on 2016/9/25.
 */
public class NewsListLoader extends CursorLoader {
    private String mChannelId;
    private int mCurrentPage = 1;
    private boolean mRefresh = false;
    private boolean mDBIsEmpty = false;

    public void setRefresh(boolean refresh) {
        mRefresh = refresh;
    }

    public NewsListLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = null;
        if (!NetworkChecker.isOnLine(getContext())) {
            return   loadFromLocal();

        }

        if (mRefresh ) {
                return fetchFromRemote();

        }
        if(mDBIsEmpty){
            cursor = fetchFromRemote();
        }else {
            cursor = loadFromLocal();
        }
        return cursor;
    }

    private void updateEmptyFlag(Cursor cursor) {
        if (cursor != null && cursor.getCount() == 0) {

            mDBIsEmpty = true;
        }else {
            mDBIsEmpty = false;
        }
    }



    private Cursor loadFromLocal() {
        Cursor cursor;
        cursor = getContext().getContentResolver().query(getUri(), getProjection(), getSelection(), getSelectionArgs(),
                getSortOrder());
        updateEmptyFlag(cursor);
        return cursor;
    }

    private Cursor fetchFromRemote() {
        Cursor cursor = null;

        return cursor;
    }
}
