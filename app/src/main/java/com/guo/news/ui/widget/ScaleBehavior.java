package com.guo.news.ui.widget;

import android.content.Context;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/11/29.
 */

public class ScaleBehavior extends CoordinatorLayout.Behavior {
    private static final int STROKE = 200;
    private static final String TAG = ScaleBehavior.class.getSimpleName();
    private static final float INIT_SCALE_DURATION = 500;
    private float mScale = 1;
    private long lastTime;

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
//        Log.d(TAG, "onNestedScroll");
        if (dyConsumed > 0) {
            mScale = Math.max(0, mScale - ((float) dyConsumed) / STROKE);
        } else {
            mScale = Math.min(1, mScale - ((float) dyConsumed) / STROKE);
        }
        child.setScaleX(mScale);
        child.setScaleY(mScale);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
//        Log.d(TAG, "onNestedFling");
        int endScale = velocityY > 0 ? 0 : 1;
        child.animate().scaleX(endScale).setDuration((long) (INIT_SCALE_DURATION * mScale)).start();
        child.animate().scaleY(endScale).setDuration((long) (INIT_SCALE_DURATION * mScale)).start();
        mScale = endScale;
        return true;
    }
}
