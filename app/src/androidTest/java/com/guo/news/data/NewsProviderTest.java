package com.guo.news.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.guo.news.data.local.NewsContract.CommentEntity;
import com.guo.news.data.local.NewsContract.ContentEntity;
import com.guo.news.data.local.NewsContract.SectionEntity;
import com.guo.news.data.local.NewsDBOpenHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.guo.news.data.TestData.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Administrator on 2016/11/9.
 */
@RunWith(AndroidJUnit4.class)
public class NewsProviderTest {

    private Context mContext = InstrumentationRegistry.getTargetContext();

    @Before
    public void setUp() {
        deleteAllRecord();
    }

    @Test
    public void insertToTableContent() {

        ContentValues cv = produceDummyContentValue();
        Uri insertedUri = mContext.getContentResolver().insert(ContentEntity.CONTENT_URI, cv);
        assertNotNull("Insert content fail", insertedUri);

        Cursor cursor = mContext.getContentResolver().query(ContentEntity.CONTENT_URI, null, null, null, ContentEntity.COLUMN_WEB_PUBLICATION_DATE + " desc");
        assertNotNull("Content query cursor is null", cursor);
        assertTrue("Content query cursor is empty", cursor.moveToFirst());
    }

    @Test
    public void deleteFromTableContent() {
        ContentValues cv = produceDummyContentValue();
        Uri insertedUri = mContext.getContentResolver().insert(ContentEntity.CONTENT_URI, cv);
        assertNotNull("Table content insert  fail", insertedUri);

        int deletedRows = mContext.getContentResolver().delete(ContentEntity.CONTENT_URI, null, null);
        assertTrue("Table content delete fail", deletedRows > 0);
    }

    @Test
    public void updateFromTableContent() {
        ContentValues cv = produceDummyContentValue();
        Uri insertedUri = mContext.getContentResolver().insert(ContentEntity.CONTENT_URI, cv);
        assertNotNull("Table content insert  fail", insertedUri);

        int rows = mContext.getContentResolver().update(ContentEntity.CONTENT_URI, cv, null, null);
        assertTrue("Table content update fail", rows > 0);
    }



    private void deleteAllRecord() {
        NewsDBOpenHelper helper = new NewsDBOpenHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(ContentEntity.TABLE_NAME, null, null);
        db.delete(SectionEntity.TABLE_NAME, null, null);
        db.delete(CommentEntity.TABLE_NAME, null, null);
    }
}
