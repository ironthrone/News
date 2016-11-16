package com.guo.news.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/11/16.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    //px unit
    private int mDividerHeight;

    public DividerItemDecoration(int dividerHeight) {
        this.mDividerHeight = dividerHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0,0,0, mDividerHeight);
    }

}
