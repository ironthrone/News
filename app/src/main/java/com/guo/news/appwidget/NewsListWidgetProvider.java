package com.guo.news.appwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.guo.news.R;

/**
 * Created by Administrator on 2016/11/13.
 */
public class NewsListWidgetProvider extends AppWidgetProvider {
    private static final String TAG = NewsListWidgetProvider.class.getSimpleName();

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(TAG, "onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for(int i = 0; i < appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];

//            PendingIntent setPendingIntent = PendingIntent.getActivity(context,0,)
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.news_app_widget);
//            remoteViews.setOnClickPendingIntent(R.id.setting,);

            Intent adapterIntent = new Intent(context,NewsListRemoteViewService.class);


            remoteViews.setRemoteAdapter(R.id.news_list,adapterIntent);

            //TODO set empty view

            appWidgetManager.updateAppWidget(appWidgetId,remoteViews);
        }

    }
}
