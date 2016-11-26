package com.guo.news.appwidget;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.guo.news.R;
import com.guo.news.data.local.NewsContract;
import com.guo.news.data.local.NewsContract.ContentEntity;
import com.guo.news.ui.NewsActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by Administrator on 2016/11/13.
 */
public class NewsListRemoteViewService extends RemoteViewsService {

    private static final String TAG = NewsListRemoteViewService.class.getSimpleName();
    public static final String KEY_SECTION_ID = "sectionId";
    private String mSectionId;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        mSectionId = intent.getStringExtra(KEY_SECTION_ID);
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
            String[] projection = {ContentEntity.COLUMN_HEADLINE,
                    ContentEntity.COLUMN_THUMBNAIL,
                    ContentEntity.COLUMN_BYLINE,
                    ContentEntity.COLUMN_ID};
            String where = NewsContract.SectionEntity.COLUMN_INSTERTED + " = ?";
            String[] whereArgs = new String[]{"1"};
            mCursor = getContentResolver().query(ContentEntity.buildContentWithSectionUri(mSectionId),
                    projection,
                    where,
                    whereArgs,
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
                String byline = mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_BYLINE));
                remoteViews.setTextViewText(R.id.headline, headline);
                remoteViews.setTextViewText(R.id.byline, byline);
                try {
                    Bitmap bitmap = Picasso.with(getApplicationContext())
                            .load(thumbnailUrl)
                            .resize(300, 200)
                            .get();
                    remoteViews.setImageViewBitmap(R.id.thumbnail, bitmap);

                    Intent fillInIntent = new Intent(getApplicationContext(), NewsActivity.class);
                    fillInIntent.putExtra(NewsActivity.KEY_CONTENT_ID, contentId);
                    remoteViews.setOnClickFillInIntent(R.id.container,fillInIntent);
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
