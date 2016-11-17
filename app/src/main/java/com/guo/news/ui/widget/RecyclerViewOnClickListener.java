package com.guo.news.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/11/17.
 */
public class RecyclerViewOnClickListener extends RecyclerView.SimpleOnItemTouchListener {

    private final OnItemClickListener mOnItemClickListener;
    private final GestureDetector mGestureDetector;

    public interface OnItemClickListener {
        void onItemClick(RecyclerView recyclerView,int position);
    }

    public RecyclerViewOnClickListener(Context context,OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        super.onTouchEvent(rv, e);
        View itemView = rv.findChildViewUnder(e.getX(), e.getY());

        if (itemView != null && mGestureDetector.onTouchEvent(e)) {
            mOnItemClickListener.onItemClick(rv,rv.getChildAdapterPosition(itemView));
        }

    }
}
