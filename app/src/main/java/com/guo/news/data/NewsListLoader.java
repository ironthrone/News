package com.guo.news.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.widget.Toast;

import com.guo.news.data.local.NewsContract.ContentEntity;
import com.guo.news.data.model.NewsModel;
import com.guo.news.data.model.PageModel;
import com.guo.news.data.model.ResultModel;
import com.guo.news.data.remote.Service;
import com.guo.news.data.remote.ServiceHost;
import com.guo.news.util.NetworkChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Administrator on 2016/9/25.
 */
public class NewsListLoader extends CursorLoader {
    private String mChannelId;
    private int mCurrentPage = 1;
    private boolean mRefresh = false;
    private boolean mDBIsEmpty = false;

    public void setRefresh(boolean refresh) {
        mRefresh = refresh;
    }

    public NewsListLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = null;
        if (!NetworkChecker.isOnLine(getContext())) {
            return   loadFromLocal();

        }

        if (mRefresh ) {
                return fetchFromRemote();

        }
        if(mDBIsEmpty){
            cursor = fetchFromRemote();
        }else {
            cursor = loadFromLocal();
        }
        return cursor;
    }

    private void updateEmptyFlag(Cursor cursor) {
        if (cursor != null && cursor.getCount() == 0) {

            mDBIsEmpty = true;
        }else {
            mDBIsEmpty = false;
        }
    }



    private Cursor loadFromLocal() {
        Cursor cursor;
        cursor = getContext().getContentResolver().query(getUri(), getProjection(), getSelection(), getSelectionArgs(),
                getSortOrder());
        updateEmptyFlag(cursor);
        return cursor;
    }

    private Cursor fetchFromRemote() {
        Cursor cursor = null;
        Service service = ServiceHost.getService();
            try {
                ResultModel<PageModel> resultModel = service.getNewsFromChannel(mChannelId, mCurrentPage, null).execute().body();
                if (resultModel.showapi_res_code == 0) {
                    ArrayList<NewsModel> newsList = resultModel.showapi_res_body.contentlist;

                    getContext().getContentResolver().bulkInsert(ContentEntity.CONTENT_URI, convertToCvs(newsList));

                    if (mRefresh) {
                        mRefresh = false;
                    }

                } else {
                    Toast.makeText(getContext(), resultModel.showapi_res_error, Toast.LENGTH_SHORT).show();
                }
                    cursor =  loadFromLocal();

            } catch (IOException e) {
                e.printStackTrace();
            }
        return cursor;
    }


    private ContentValues[] convertToCvs(ArrayList<NewsModel> newsList) {

        ContentValues[] cvs = new ContentValues[newsList.size()];
        for (NewsModel newsModel : newsList) {

            ContentValues cv = new ContentValues();
            cv.put(ContentEntity.TITLE,newsModel.title);
            cv.put(ContentEntity.COLUMN_STANDFIRST,newsModel.source);
            cv.put(ContentEntity.COLUMN_SECTION_ID,newsModel.channelId);
            cv.put(ContentEntity.COLUMN_BYLINE,newsModel.channelName);
            cv.put(ContentEntity.COLUMN_WORD_COUNT,newsModel.content);
            cv.put(ContentEntity.DESC,newsModel.desc);
            cv.put(ContentEntity.COLUMN_BODY,newsModel.havePic);
            cv.put(ContentEntity.COLUMN_WEB_URL,newsModel.link);
            if (newsModel.imageurls.size() >= 0) {
                cv.put(ContentEntity.MAIN_PIC,newsModel.imageurls.get(0).url);
            }
            if (Util.getTimeStamp(newsModel.pubDate) > 0) {

                cv.put(ContentEntity.COLUMN_THUMBNAIL,Util.getTimeStamp(newsModel.pubDate));
            }

            Arrays.fill(cvs,cv);
        }
        return cvs;
    }

}
