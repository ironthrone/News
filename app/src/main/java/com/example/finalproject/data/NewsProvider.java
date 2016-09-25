package com.example.finalproject.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.finalproject.data.NewsContract.ChannelEntity;
import com.example.finalproject.data.NewsContract.CommentEntity;
import com.example.finalproject.data.NewsContract.ImageEntity;
import com.example.finalproject.data.NewsContract.NewsEntity;

import static com.example.finalproject.app.Constant.QUREY_AMOUNT;

/**
 * Created by Administrator on 2016/9/24.
 */
public class NewsProvider extends ContentProvider {


    private static final int NEWS = 100;
    private static final int NEWS_WITH_ID = 101;
    private static final int NEWS_WITH_PAGE = 110;
    private static final int COMMENT = 200;
    private static final int COMMENT_ID = 201;
    private static final int IMAGE = 300;
    private static final int CHANNEL = 400;
    private DatabaseHelper mDatabaseHelper;
    private static final UriMatcher mUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder mNewsByIdQueryBuilder;

    static {
        mNewsByIdQueryBuilder = new SQLiteQueryBuilder();
        mNewsByIdQueryBuilder.setTables(NewsEntity.TABLE_NAME + "inner join" +
                ImageEntity.TABLE_NAME + " on" + NewsEntity._ID +
                "=" + ImageEntity.NEWS_ID);
    }


    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        //insert query news
        matcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.NEWS_PATH, NEWS);
        //query single news
        matcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.NEWS_PATH + "/*", NEWS_WITH_ID);
        matcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.NEWS_PATH + "/page/*", NEWS_WITH_PAGE);

        matcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.IMAGE_PATH, IMAGE);

        matcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.CHANNEL_PATH, CHANNEL);

        //query comment list
        matcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.COMMENT_PATH, COMMENT);
        //insert single comment
        matcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.COMMENT_PATH + "/*", COMMENT_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case NEWS:
                return NewsEntity.CONTENT_TYPE;
            case NEWS_WITH_PAGE:
                return NewsEntity.CONTENT_TYPE;
            case NEWS_WITH_ID:
                return NewsEntity.CONTENT_ITEM_TYPE;

            case CHANNEL:
                return NewsEntity.CONTENT_TYPE;

            case IMAGE:
                return ImageEntity.CONTENT_TYPE;

            case COMMENT:
                return CommentEntity.CONTENT_TYPE;
            case COMMENT_ID:
                return CommentEntity.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri :" + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;
        switch (mUriMatcher.match(uri)) {
            case CHANNEL:
                cursor = mDatabaseHelper.getReadableDatabase().query(ChannelEntity.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
break;
            case NEWS_WITH_PAGE:
                cursor = mDatabaseHelper.getReadableDatabase().query(NewsEntity.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder,
                        getPageLimit(NewsEntity.getPage(uri)));
                break;
            case NEWS_WITH_ID:
                cursor = mNewsByIdQueryBuilder.query(mDatabaseHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case COMMENT:
                cursor = mDatabaseHelper.getReadableDatabase().query(CommentEntity.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder,
                        getPageLimit(1));
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);

        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long insertedId = -1;
        switch (mUriMatcher.match(uri)) {
            case CHANNEL:
                insertedId = mDatabaseHelper.getWritableDatabase().insert(ChannelEntity.TABLE_NAME,
                        null,
                        values);
                break;
            case NEWS:
                insertedId = mDatabaseHelper.getWritableDatabase().insert(NewsEntity.TABLE_NAME,
                        null,
                        values);
                break;
            case IMAGE:
                insertedId = mDatabaseHelper.getWritableDatabase().insert(ImageEntity.TABLE_NAME,
                        null,
                        values);
                break;
            case COMMENT:
                insertedId = mDatabaseHelper.getWritableDatabase().insert(CommentEntity.TABLE_NAME,
                        null,
                        values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);



        }

        return insertedId >0 ? uri : null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletedRow = 0;
        switch (mUriMatcher.match(uri)) {
            case NEWS:
                deletedRow = mDatabaseHelper.getWritableDatabase().delete(NewsEntity.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case IMAGE:
                deletedRow = mDatabaseHelper.getWritableDatabase().delete(ImageEntity.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case COMMENT:
                deletedRow = mDatabaseHelper.getWritableDatabase().delete(CommentEntity.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);



        }
        return deletedRow;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updatedRow = 0;
        switch (mUriMatcher.match(uri)) {
            case NEWS:
                updatedRow = mDatabaseHelper.getWritableDatabase().update(NewsEntity.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case IMAGE:
                updatedRow = mDatabaseHelper.getWritableDatabase().update(ImageEntity.TABLE_NAME,
                        values,

                        selection,
                        selectionArgs);
                break;
            case COMMENT:
                updatedRow = mDatabaseHelper.getWritableDatabase().update(CommentEntity.TABLE_NAME,
                        values,

                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);

        }
        return 0;
    }

    private static String getPageLimit(int page) {
        String limit = null;
        if (page == 1) {
            limit = "limit " + page * QUREY_AMOUNT;
        } else {
            limit = "limit " + (page - 1) * QUREY_AMOUNT + "," + page * QUREY_AMOUNT;
        }
        return limit;
    }
}
