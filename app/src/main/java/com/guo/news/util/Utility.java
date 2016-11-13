package com.guo.news.util;

import android.content.ContentValues;
import android.content.Context;

import com.guo.news.data.local.NewsContract;
import com.guo.news.data.local.NewsContract.ContentEntity;
import com.guo.news.data.model.ContentModel;
import com.guo.news.data.model.SectionModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2016/9/25.
 */
public class Utility {
    public static long getTimeStamp(String dateStr) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date == null ? -1 : date.getTime();

    }

    public static int insertSections(Context context, List<SectionModel> sectionModels) {
        ContentValues[] contentValuesArray = new ContentValues[sectionModels.size()];
        for (int i = 0; i < sectionModels.size();i++) {
            SectionModel sectionModel = sectionModels.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(NewsContract.SectionEntity.COLUMN_ID,sectionModel.id);
            contentValues.put(NewsContract.SectionEntity.COLUMN_WEB_TITLE,sectionModel.webTitle);
            contentValuesArray[i] = contentValues;
        }
        return context.getContentResolver().bulkInsert(NewsContract.SectionEntity.CONTENT_URI, contentValuesArray);
    }

    public static int insertContents(Context context,List<ContentModel> contentModels) {
        ContentValues[] contentValuesArray = new ContentValues[contentModels.size()];
//        DateFormat df = new SimpleDateFormat("yyyy-MM-hh hh:mm:ss");
        for (int i = 0; i < contentModels.size() ; i++ ) {
            ContentModel contentModel = contentModels.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(ContentEntity.COLUMN_ID, contentModel.id);
            contentValues.put(ContentEntity.COLUMN_SECTION_ID, contentModel.sectionId);
//            try {
//                String formattedTime = contentModel.webPublicationDate.replaceAll("A-Z", " ");
//                long publishTime = df.parse(formattedTime).getTime();
//                contentValue.put(ContentEntity.COLUMN_WEB_PUBLICATION_DATE, publishTime);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            contentValues.put(ContentEntity.COLUMN_WEB_PUBLICATION_DATE, contentModel.webPublicationDate);
            contentValues.put(ContentEntity.COLUMN_WEB_URL, contentModel.webUrl);
            contentValues.put(ContentEntity.COLUMN_HEADLINE, contentModel.fields.headline);
            contentValues.put(ContentEntity.COLUMN_STANDFIRST, contentModel.fields.standfirst);
            contentValues.put(ContentEntity.COLUMN_BODY, contentModel.fields.body);
            contentValues.put(ContentEntity.COLUMN_BYLINE, contentModel.fields.byline);
            contentValues.put(ContentEntity.COLUMN_THUMBNAIL, contentModel.fields.thumbnail);
            contentValues.put(ContentEntity.COLUMN_WORD_COUNT, contentModel.fields.wordcount);
            contentValuesArray[i] = contentValues;
        }
        return context.getContentResolver().bulkInsert(ContentEntity.CONTENT_URI, contentValuesArray);
    }

}
