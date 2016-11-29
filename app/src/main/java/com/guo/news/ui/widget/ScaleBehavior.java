package com.guo.news.ui.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/11/29.
 */

public class ScaleBehavior extends CoordinatorLayout.Behavior {
    private static final int STROKE = 200;
    private float mScale = 1;

    public ScaleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
//        if (child instanceof FloatingActionButton) {
//            return true;
//        }
//        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyConsumed > 0) {
            mScale = Math.max(0, mScale - ((float) dyConsumed) / STROKE);
        } else {
            mScale = Math.min(1,mScale - ((float) dyConsumed) / STROKE);
        }
        child.setScaleX(mScale);
        child.setScaleY(mScale);
    }
}
