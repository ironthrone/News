package com.guo.news.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.guo.news.data.local.NewsContract.SectionEntity.CREATE_CHANNEL_TABLE;
import static com.guo.news.data.local.NewsContract.CommentEntity.CREATE_COMMENT_TABLE;
import static com.guo.news.data.local.NewsContract.ImageEntity.CREATE_IMAGE_TABLE;
import static com.guo.news.data.local.NewsContract.ContentEntity.CREATE_NEWS_TABLE;

/**
 * Created by Administrator on 2016/9/24.
 */
public class NewsDBOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "news.db";


    public NewsDBOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEWS_TABLE);
        db.execSQL(CREATE_COMMENT_TABLE);
        db.execSQL(CREATE_IMAGE_TABLE);
        db.execSQL(CREATE_CHANNEL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
