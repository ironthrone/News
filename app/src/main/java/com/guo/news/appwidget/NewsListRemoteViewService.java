package com.guo.news.appwidget;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.guo.news.PreferenceConstant;
import com.guo.news.R;
import com.guo.news.data.local.NewsContract;
import com.guo.news.data.local.NewsContract.ContentEntity;
import com.guo.news.data.model.ContentModel;
import com.guo.news.ui.NewsActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/13.
 */
public class NewsListRemoteViewService extends RemoteViewsService {

    private static final String TAG = NewsListRemoteViewService.class.getSimpleName();
    public static final String KEY_SECTION_ID = "sectionId";
    private String mSectionId;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NewsListRemoteViewFactory();
    }

    private class NewsListRemoteViewFactory implements RemoteViewsFactory {

        private List<ContentModel> mContents;

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            Log.d(TAG, "onDataChange");
            mSectionId = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(PreferenceConstant.KEY_APP_WIDGET_SECTION, PreferenceConstant.APP_WIDGET_SECTION_DEFAULT);
            String[] projection = {ContentEntity.COLUMN_HEADLINE,
                    ContentEntity.COLUMN_THUMBNAIL,
                    ContentEntity.COLUMN_BYLINE,
                    ContentEntity.COLUMN_ID};
            String where = NewsContract.SectionEntity.COLUMN_INSTERTED + " = ?";
            String[] whereArgs = new String[]{"1"};
            Cursor cursor = getContentResolver().query(ContentEntity.buildContentWithSectionUri(mSectionId),
                    projection,
                    where,
                    whereArgs,
                    null);
            mContents = new ArrayList<>();

            if (cursor != null && cursor.moveToFirst()) {
                try {
                    do {
                        ContentModel contentModel = new ContentModel();
                        contentModel.fields = new ContentModel.FieldModel();
                        contentModel.fields.thumbnail = cursor.getString(cursor.getColumnIndex(ContentEntity.COLUMN_THUMBNAIL));
                        contentModel.fields.headline = cursor.getString(cursor.getColumnIndex(ContentEntity.COLUMN_HEADLINE));
                        contentModel.fields.byline = cursor.getString(cursor.getColumnIndex(ContentEntity.COLUMN_BYLINE));
                        contentModel.id = cursor.getString(cursor.getColumnIndex(ContentEntity.COLUMN_ID));
                        mContents.add(contentModel);
                    } while (cursor.moveToNext());
                } finally {
                    cursor.close();
                }
            }
        }

        @Override
        public void onDestroy() {
            if (mContents != null) {
                mContents = null;
            }
        }

        @Override
        public int getCount() {
            return mContents.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.item_app_widget);
            ContentModel contentModel = mContents.get(position);
            remoteViews.setTextViewText(R.id.headline, contentModel.fields.headline);
            remoteViews.setTextViewText(R.id.byline, contentModel.fields.byline);
            try {
                Bitmap bitmap = Picasso.with(getApplicationContext())
                        .load(contentModel.fields.thumbnail)
                        .resize(300, 200)
                        .get();
                remoteViews.setImageViewBitmap(R.id.thumbnail, bitmap);

                Intent fillInIntent = new Intent(getApplicationContext(), NewsActivity.class);
                fillInIntent.putExtra(NewsActivity.KEY_CONTENT_ID, contentModel.id);
                remoteViews.setOnClickFillInIntent(R.id.container, fillInIntent);
            } catch (IOException e) {
                e.printStackTrace();
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
