package com.guo.news.ui.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/7/15.
 */
public class MovementBehavior extends CoordinatorLayout.Behavior {
    private static final String TAG = MovementBehavior.class.getSimpleName();
    private  Context mContext;
    private final DisplayMetrics displayMetrics;
    //init y ,before nested scroll
    private float initY;
    private boolean show;


    public MovementBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        if(initY == 0){

        initY = child.getY();
        }
        return true;
    }


    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        float newY = child.getY() + dyConsumed/2;
        if(dyConsumed >0 && show){

            ViewCompat.animate(child)
                    .setDuration(300)
                    .translationY(displayMetrics.heightPixels)
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {

                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            show = false;
                        }

                        @Override
                        public void onAnimationCancel(View view) {

                        }
                    })
            .start();
        }else if(dyConsumed < 0 && !show){
            ViewCompat.animate(child)
                    .setDuration(300)
                    .translationY(0)
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {

                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            show = true;
                        }

                        @Override
                        public void onAnimationCancel(View view) {

                        }
                    })
                    .start();
        }
    }

}
