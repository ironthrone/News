package com.guo.news.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.guo.news.data.local.NewsContract;
import com.guo.news.data.local.NewsDBOpenHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

/**
 * Created by Administrator on 2016/11/9.
 */
@RunWith(AndroidJUnit4.class)
public class NewsDBTest {

    private Context mContext = InstrumentationRegistry.getTargetContext();
    private NewsDBOpenHelper mHelper;

    @Before
    public void setUp() {
        mContext.deleteDatabase(NewsDBOpenHelper.DB_NAME);
        mHelper = new NewsDBOpenHelper(mContext);
    }

    @Test
    public void createAndDeleteDB() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        assertTrue("News db not successfully opened",db.isOpen());

        boolean deleted = mContext.deleteDatabase(NewsDBOpenHelper.DB_NAME);
        assertTrue("News DB is not deleted successfully",deleted);
    }

    @Test
    public void insertToTableContent() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long rowId = db.insert(NewsContract.ContentEntity.TABLE_NAME, null, TestData.produceDummyContentValue());
        assertTrue("Table content insert fail",rowId > 0);
    }
}
