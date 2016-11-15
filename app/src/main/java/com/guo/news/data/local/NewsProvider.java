package com.guo.news.data.local;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.guo.news.data.local.NewsContract.CommentEntity;
import com.guo.news.data.local.NewsContract.ContentEntity;
import com.guo.news.data.local.NewsContract.SectionEntity;

/**
 * Created by Administrator on 2016/9/24.
 */
public class NewsProvider extends ContentProvider {


    public static final int SECTION = 100;

    public static final int CONTENT = 200;
    public static final int CONTENT_WITH_ID = 201;
    public static final int CONTENT_WITH_SECTION = 202;

    public static final int COMMENT = 300;
    public static final int COMMENT_WITH_CONTENT = 301;

    private NewsDBOpenHelper mDatabaseHelper;
    private static final UriMatcher mUriMatcher = buildUriMatcher();


    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_CONTENT, CONTENT);
        matcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_CONTENT + "/item/*", CONTENT_WITH_ID);
        matcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_CONTENT + "/*", CONTENT_WITH_SECTION);


        matcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_SECTION, SECTION);

        matcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_COMMENT, COMMENT);
        matcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_COMMENT + "/*", COMMENT_WITH_CONTENT);
        return matcher;
    }

    public static UriMatcher getUriMatcher() {
        return mUriMatcher;
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new NewsDBOpenHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case CONTENT:
                return ContentEntity.CONTENT_TYPE;
            case CONTENT_WITH_SECTION:
                return ContentEntity.CONTENT_TYPE;
            case CONTENT_WITH_ID:
                return ContentEntity.CONTENT_ITEM_TYPE;

            case SECTION:
                return ContentEntity.CONTENT_TYPE;

            case COMMENT:
                return CommentEntity.CONTENT_TYPE;
            case COMMENT_WITH_CONTENT:
                return CommentEntity.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri :" + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;
        switch (mUriMatcher.match(uri)) {
            case SECTION:
                cursor = mDatabaseHelper.getReadableDatabase().query(SectionEntity.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CONTENT:
                cursor = mDatabaseHelper.getReadableDatabase().query(ContentEntity.TABLE_NAME,
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
                        sortOrder);
                break;
            case CONTENT_WITH_SECTION:
            {
                String sectionId = uri.getLastPathSegment();
                selection = ContentEntity.COLUMN_SECTION_ID + " = ?";
                selectionArgs = new String[]{sectionId};
                cursor = mDatabaseHelper.getReadableDatabase().query(ContentEntity.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case CONTENT_WITH_ID:
            {
                String contentId = uri.getLastPathSegment();
                selection = ContentEntity.COLUMN_ID + " = ? ";
                selectionArgs = new String[]{contentId};
                cursor = mDatabaseHelper.getReadableDatabase().query(ContentEntity.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case COMMENT_WITH_CONTENT:
            {
                String contentId = uri.getLastPathSegment();
                selection = CommentEntity.COLUMN_CONTENT_ID +  " = ? ";
                selectionArgs = new String[]{contentId};
                cursor = mDatabaseHelper.getReadableDatabase().query(CommentEntity.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unsupported uri:" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long insertedId = -1;
        Uri returnUri;

        switch (mUriMatcher.match(uri)) {
            case SECTION:
                insertedId = mDatabaseHelper.getWritableDatabase().insert(SectionEntity.TABLE_NAME,
                        null,
                        values);
                if (insertedId > 0) {
                    returnUri = SectionEntity.CONTENT_URI.buildUpon().appendPath(String.valueOf(insertedId)).build();
                } else {
                    throw new SQLException("Fail to inset to section " + uri);
                }
                break;
            case CONTENT:
                insertedId = mDatabaseHelper.getWritableDatabase().insert(ContentEntity.TABLE_NAME,
                        null,
                        values);
                if (insertedId > 0) {
                    returnUri = ContentEntity.CONTENT_URI.buildUpon().appendPath(String.valueOf(insertedId)).build();
                } else {
                    throw new SQLException("Fail to inset to content " + uri);

                }
                break;
            case COMMENT:
                insertedId = mDatabaseHelper.getWritableDatabase().insert(CommentEntity.TABLE_NAME,
                        null,
                        values);
                if (insertedId > 0) {
                    returnUri = CommentEntity.CONTENT_URI.buildUpon().appendPath(String.valueOf(insertedId)).build();
                } else {
                    throw new SQLException("Fail to inset to comment " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unsupported uri:" + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletedRow = 0;
        switch (mUriMatcher.match(uri)) {
            case SECTION:
                deletedRow = mDatabaseHelper.getWritableDatabase().delete(SectionEntity.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CONTENT:
                deletedRow = mDatabaseHelper.getWritableDatabase().delete(ContentEntity.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case COMMENT:
                deletedRow = mDatabaseHelper.getWritableDatabase().delete(CommentEntity.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported uri:" + uri);


        }
        if (deletedRow > 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return deletedRow;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updatedRow = 0;
        switch (mUriMatcher.match(uri)) {
            case SECTION:
                updatedRow = mDatabaseHelper.getWritableDatabase().update(ContentEntity.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);

                break;
            case CONTENT:
                updatedRow = mDatabaseHelper.getWritableDatabase().update(ContentEntity.TABLE_NAME,
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
        if (updatedRow > 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return updatedRow;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int insertedRows = -1;
        switch (mUriMatcher.match(uri)) {
            case SECTION:
                db.beginTransaction();
                try {
                    for (ContentValues cv : values) {
                        long insertId = db.insert(SectionEntity.TABLE_NAME, null, cv);
                        if (insertId > 0) {
                            insertedRows++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                break;
            case CONTENT:
                db.beginTransaction();
                try {
                    for (ContentValues cv : values) {
                        long insertId = db.insert(ContentEntity.TABLE_NAME, null, cv);
                        if (insertId > 0) {
                            insertedRows++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                break;
            case COMMENT:
                db.beginTransaction();
                try {
                    for (ContentValues cv : values) {
                        long insertId = db.insert(CommentEntity.TABLE_NAME, null, cv);
                        if (insertId > 0) {
                            insertedRows++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
        if (insertedRows > 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return insertedRows;
    }
}
