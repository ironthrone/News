package com.guo.news;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Administrator on 2016/11/29.
 */

public class NewsApplication extends Application {
    private Tracker mTracker;

    public Tracker getTracker() {
        startTracker();
        return mTracker;
    }

    public void startTracker() {
        if (mTracker == null) {
            GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(this);
            mTracker = googleAnalytics.newTracker(R.xml.ga_tracker);
            googleAnalytics.enableAutoActivityReports(this);
            googleAnalytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        }
    }
}
