package com.guo.news.ui.widget;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2016/11/13.
 */
public abstract class RecyclerViewCursorAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{

    protected final Context mContext;
    protected Cursor mCursor;

    public RecyclerViewCursorAdapter(Context context, Cursor cursor) {
        mCursor = cursor;
        mContext = context;
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        if (mCursor.moveToPosition(position)) {
            onBindViewHolder(holder,mCursor);
        }
    }

    public abstract void onBindViewHolder(VH holder, Cursor cursor);

    @Override
    public int getItemCount() {
        if(mCursor == null) return 0;
        return mCursor.getCount();
    }

    public Cursor getItem(int position) {
        if (mCursor.moveToPosition(position)) {
            return mCursor;
        }

        return null;
    }
}
