package com.example.finalproject.data.local;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Administrator on 2016/9/24.
 */
public class NewsContract {

    public static final String CONTENT_AUTHORITY = "com.guo.news";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String NEWS_PATH = "news";
    public static final String COMMENT_PATH = "comment";
    public static final String IMAGE_PATH = "image";
    public static final String CHANNEL_PATH = "channel";

    public static Uri buildNewsUri() {
        return Uri.withAppendedPath(BASE_CONTENT_URI, NEWS_PATH);
    }

    public static Uri buildCommentUri() {
        return Uri.withAppendedPath(BASE_CONTENT_URI, COMMENT_PATH);
    }

    private static String getContentType(String path) {
        return ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/vnd." + CONTENT_AUTHORITY + "." + path;
    }

    private static String getContentItemType(String path) {
        return ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/vnd." + CONTENT_AUTHORITY + "." + path;
    }



    public static class NewsEntity implements BaseColumns {

        public static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, NEWS_PATH);

        public static final String TABLE_NAME = "news";

        public static final String CONTENT_TYPE = getContentType(NEWS_PATH);
        public static final String CONTENT_ITEM_TYPE = getContentItemType(NEWS_PATH);

        public static final String TITLE = "title";
        public static final String DESC = "desc";
        public static final String CHANNEL_ID = "channelId";
        public static final String LINK = "link";
        public static final String SOURCE = "source";
        public static final String CHANNEL_NAME = "channelName";
        public static final String HAVE_PIC = "havePic";
        public static final String PUB_DATE = "pubDate";
        public static final String CONTENT = "content";
        public static final String MAIN_PIC = "mainPic";
        //TODO allList store parse

        public static final String CREATE_NEWS_TABLE = "create table " + TABLE_NAME + "(" +
                _ID + " integer primary key autoincrement," +
                TITLE + " text unique not null," +
                CHANNEL_ID + "text not null," +
                DESC + " text," +
                LINK + " text not null," +
                CHANNEL_NAME + " text not null," +
                HAVE_PIC + " integer," +
                CONTENT + " text," +
                PUB_DATE + " integer not null," +
                SOURCE + "text," +
                MAIN_PIC + "text," +
                " on conflict ignore" +
                " foreign key(" + CHANNEL_ID + ") references " +
                ChannelEntity.TABLE_NAME + "(" + ChannelEntity._ID + ")" +
                ");";

        public static int getPage(Uri uri) {
            return Integer.parseInt(uri.getLastPathSegment());
        }


        public static Uri newsWithPageUri(int page){
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(page)).build();
        }
        public static Uri newsWithIdUri(int id){
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }

    }

    public static class ChannelEntity implements BaseColumns {
        public static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, CHANNEL_PATH);


        public static final String TABLE_NAME = "channel";


        public static final String CONTENT_TYPE = getContentType(CHANNEL_PATH);
        public static final String CONTENT_ITEM_TYPE = getContentItemType(CHANNEL_PATH);

        public static final String NAME = "name";


        public static final String CREATE_CHANNEL_TABLE = "create table " + TABLE_NAME + "(" +
                _ID + " text primary key," +
                NAME + " text not null," +
                ");";



    }

    public static class CommentEntity implements BaseColumns {
        public static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, COMMENT_PATH);


        public static final String TABLE_NAME = "comment";


        public static final String CONTENT_TYPE = getContentType(COMMENT_PATH);
        public static final String CONTENT_ITEM_TYPE = getContentItemType(COMMENT_PATH);

        public static final String NEWS_ID = "news_id";
        public static final String CONTENT = "content";
        public static final String ADD_TIME = "add_time";


        public static final String CREATE_COMMENT_TABLE = "create table " + TABLE_NAME + "(" +
                _ID + " integer primary key autoincrement," +
                NEWS_ID + " integer not null," +
                CONTENT + " text not null," +
                ADD_TIME + " integer not null," +
                "foreign key(" + NEWS_ID + ") references " +
                NewsEntity.TABLE_NAME + "(" + NewsEntity._ID + ")" +
                ");";
        public static Uri commentWithIdUri(int id){
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }


    }


    public static class ImageEntity implements BaseColumns {
        public static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, IMAGE_PATH);

        public static final String TABLE_NAME = "image";

        public static final String CONTENT_TYPE = getContentType(IMAGE_PATH);
        public static final String CONTENT_ITEM_TYPE = getContentItemType(IMAGE_PATH);


        public static final String NEWS_ID = "news_id";
        public static final String HEIGHT = "height";
        public static final String WIDTH = "width";
        public static final String URL = "url";

        public static final String CREATE_IMAGE_TABLE = "create table " + TABLE_NAME + "(" +
                _ID + " integer primary key autoincrement," +
                NEWS_ID + " integer not null," +
                HEIGHT + " integer not null," +
                WIDTH + " integer not null," +
                URL + " text not null," +
                "foreign key(" + NEWS_ID + ") references " +
                NewsEntity.TABLE_NAME + "(" + NewsEntity._ID + ")" +
                ");";
    }



}
