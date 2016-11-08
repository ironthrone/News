package com.guo.news.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.guo.news.data.local.NewsContract.CommentEntity;
import com.guo.news.data.local.NewsContract.ContentEntity;
import com.guo.news.data.local.NewsContract.SectionEntity;

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
        final String CREATE_CONTENT_TABLE = "create table " + ContentEntity.TABLE_NAME + "(" +
                ContentEntity._ID + " integer primary key autoincrement," +
                ContentEntity.COLUMN_ID + " text unique not null," +
                ContentEntity.COLUMN_HEADLINE + " text not null," +
                ContentEntity.COLUMN_SECTION_ID + "text not null," +
                ContentEntity.COLUMN_WEB_PUBLICATION_DATE + " text not null," +
                ContentEntity.COLUMN_WEB_URL + " text not null," +
                ContentEntity.COLUMN_BYLINE + " text not null," +
                ContentEntity.COLUMN_BODY + " integer," +
                ContentEntity.COLUMN_WORD_COUNT + " text," +
                ContentEntity.COLUMN_THUMBNAIL + " integer not null," +
                ContentEntity.COLUMN_STANDFIRST + "text," +
                " on conflict ignore" +
                " foreign key(" + ContentEntity.COLUMN_SECTION_ID + ") references " +
                SectionEntity.TABLE_NAME + "(" + SectionEntity._ID + ")" +
                ");";
        final String CREATE_COMMENT_TABLE = "create table " + CommentEntity.TABLE_NAME + "(" +
                CommentEntity._ID + " integer primary key autoincrement," +
                CommentEntity.COLUMN_ID + "integer unique not null" +
                CommentEntity.COLUMN_CONTENT_ID + " integer unique not null," +
                CommentEntity.COLUMN_CONTENT + " text not null," +
                CommentEntity.COLUMN_ADD_TIME + " integer not null," +
                "on conflict ignore " +
                "foreign key(" + CommentEntity.COLUMN_CONTENT_ID + ") references " +
                ContentEntity.TABLE_NAME + "(" + ContentEntity._ID + ")" +
                ");";

        final String CREATE_SECTION_TABLE = "create table " + SectionEntity.TABLE_NAME + "(" +
                SectionEntity._ID + " text primary key," +
                SectionEntity.COLUMN_ID + " text unique not null," +
                SectionEntity.COLUMN_WEB_TITLE + " text not null," +
                "on conflict ignore" +
                ");";
        db.execSQL(CREATE_COMMENT_TABLE);
        db.execSQL(CREATE_CONTENT_TABLE);
        db.execSQL(CREATE_SECTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
