package com.guo.news.appwidget;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.guo.news.PreferenceConstant;
import com.guo.news.R;
import com.guo.news.data.local.NewsContract.ContentEntity;
import com.guo.news.ui.NewsActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by Administrator on 2016/11/13.
 */
public class NewsListRemoteViewService extends RemoteViewsService {

    private static final String TAG = NewsListRemoteViewService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NewsListRemoteViewFactory();
    }

    private class NewsListRemoteViewFactory implements RemoteViewsFactory {

        private Cursor mCursor;

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            Log.d(TAG, "onDataChange");
            SharedPreferences mainPreference = getSharedPreferences(PreferenceConstant.MAIN_PREFERENCE, MODE_PRIVATE);
            String sectionId = mainPreference.getString(PreferenceConstant.KEY_APP_WIDGET_SECTION, PreferenceConstant.APP_WIDGET_SECTION_DEFAULT);
            String[] projection = {ContentEntity.COLUMN_HEADLINE, ContentEntity.COLUMN_THUMBNAIL,ContentEntity.COLUMN_ID};
            mCursor = getContentResolver().query(ContentEntity.buildContentWithSectionUri(sectionId),
                    projection,
                    null,
                    null,
                    null);
        }

        @Override
        public void onDestroy() {
            if (mCursor != null) {
                mCursor.close();
                mCursor = null;
            }
        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.item_app_widget);
            if (mCursor != null && mCursor.moveToPosition(position)) {

                String headline = mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_HEADLINE));
                String thumbnailUrl = mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_THUMBNAIL));
                String contentId = mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_ID));
                remoteViews.setTextViewText(R.id.headline, headline);
                try {
                    Bitmap bitmap = Picasso.with(getApplicationContext())
                            .load(thumbnailUrl)
                            .resize(300, 200)
                            .get();
                    remoteViews.setImageViewBitmap(R.id.thumbnail, bitmap);
                    Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                    intent.putExtra(NewsActivity.KEY_CONTENT_ID, contentId);
                    PendingIntent toActivityPending = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setOnClickPendingIntent(R.id.container, toActivityPending);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
