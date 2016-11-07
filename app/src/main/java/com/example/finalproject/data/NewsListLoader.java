package com.example.finalproject.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.widget.Toast;

import com.example.finalproject.data.local.NewsContract.NewsEntity;
import com.example.finalproject.data.model.NewsModel;
import com.example.finalproject.data.model.PageModel;
import com.example.finalproject.data.model.ResultModel;
import com.example.finalproject.data.remote.Service;
import com.example.finalproject.data.remote.ServiceHost;
import com.example.finalproject.util.NetworkChecker;

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

                    getContext().getContentResolver().bulkInsert(NewsEntity.CONTENT_URI, convertToCvs(newsList));

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
            cv.put(NewsEntity.TITLE,newsModel.title);
            cv.put(NewsEntity.SOURCE,newsModel.source);
            cv.put(NewsEntity.CHANNEL_ID,newsModel.channelId);
            cv.put(NewsEntity.CHANNEL_NAME,newsModel.channelName);
            cv.put(NewsEntity.CONTENT,newsModel.content);
            cv.put(NewsEntity.DESC,newsModel.desc);
            cv.put(NewsEntity.HAVE_PIC,newsModel.havePic);
            cv.put(NewsEntity.LINK,newsModel.link);
            if (newsModel.imageurls.size() >= 0) {
                cv.put(NewsEntity.MAIN_PIC,newsModel.imageurls.get(0).url);
            }
            if (Util.getTimeStamp(newsModel.pubDate) > 0) {

                cv.put(NewsEntity.PUB_DATE,Util.getTimeStamp(newsModel.pubDate));
            }

            Arrays.fill(cvs,cv);
        }
        return cvs;
    }

}
