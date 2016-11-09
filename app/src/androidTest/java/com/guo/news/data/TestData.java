package com.guo.news.data;

import android.content.ContentValues;

import com.guo.news.data.local.NewsContract;

/**
 * Created by Administrator on 2016/11/9.
 */
public class TestData {
    public static ContentValues produceDummyContentValue() {
        ContentValues cv = new ContentValues();
        cv.put(NewsContract.ContentEntity.COLUMN_ID, "football");
        cv.put(NewsContract.ContentEntity.COLUMN_HEADLINE, "headline");
        cv.put(NewsContract.ContentEntity.COLUMN_BODY, "body");
        cv.put(NewsContract.ContentEntity.COLUMN_BYLINE, "byline");
        cv.put(NewsContract.ContentEntity.COLUMN_SECTION_ID, "sectionId");
        cv.put(NewsContract.ContentEntity.COLUMN_STANDFIRST, "standFirst");
        cv.put(NewsContract.ContentEntity.COLUMN_WEB_PUBLICATION_DATE, "publicationDate");
        cv.put(NewsContract.ContentEntity.COLUMN_THUMBNAIL, "thumbnail");
        cv.put(NewsContract.ContentEntity.COLUMN_WEB_URL, "webUrl");
        cv.put(NewsContract.ContentEntity.COLUMN_WORD_COUNT, "wordCount");
        cv.put(NewsContract.ContentEntity.COLUMN_STANDFIRST, "standFirst");
        return cv;
    }
}
