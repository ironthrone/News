package com.guo.news.appwidget;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.guo.news.R;
import com.guo.news.data.local.NewsContract.ContentEntity;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by Administrator on 2016/11/13.
 */
public class NewsListRemoteViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NewsListRemoteViewFactory();
    }

    private class NewsListRemoteViewFactory implements RemoteViewsFactory {

        //TODO　replace
        private String mSectionId = "football";
        private Cursor mCursor;

        @Override
        public void onCreate() {
            String[] projection = {ContentEntity.COLUMN_HEADLINE, ContentEntity.COLUMN_THUMBNAIL};
            mCursor = getContentResolver().query(ContentEntity.buildContentWithSectionUri(mSectionId),
                    projection,
                    null,
                    null,
                    null);
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {
            mCursor = null;
        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.item_app_widget);
            //TODO add click intent
            if (mCursor != null && mCursor.moveToPosition(position)) {

                String headline = mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_HEADLINE));
                String thumbnailUrl = mCursor.getString(mCursor.getColumnIndex(ContentEntity.COLUMN_THUMBNAIL));
                remoteViews.setTextViewText(R.id.headline,headline);
                try {
                    Bitmap bitmap = Picasso.with(getApplicationContext())
                            .load(thumbnailUrl)
                            .resize(300,200)
                            .get();
                    remoteViews.setImageViewBitmap(R.id.thumbnail,bitmap);

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
            return 0;
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
