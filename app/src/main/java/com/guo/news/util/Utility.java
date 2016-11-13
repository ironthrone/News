package com.guo.news.util;

import android.content.ContentValues;

import com.guo.news.data.local.NewsContract.ContentEntity;
import com.guo.news.data.model.ContentModel;

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

    public static ContentValues[] convert(List<ContentModel> contentModels) {
        ContentValues[] contentValues = new ContentValues[contentModels.size()];
//        DateFormat df = new SimpleDateFormat("yyyy-MM-hh hh:mm:ss");
        for (ContentModel contentModel : contentModels) {
            ContentValues contentValue = new ContentValues();
            contentValue.put(ContentEntity.COLUMN_ID, contentModel.id);
            contentValue.put(ContentEntity.COLUMN_SECTION_ID, contentModel.sectionId);
//            try {
//                String formattedTime = contentModel.webPublicationDate.replaceAll("A-Z", " ");
//                long publishTime = df.parse(formattedTime).getTime();
//                contentValue.put(ContentEntity.COLUMN_WEB_PUBLICATION_DATE, publishTime);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
                contentValue.put(ContentEntity.COLUMN_WEB_PUBLICATION_DATE, contentModel.webPublicationDate);
            contentValue.put(ContentEntity.COLUMN_WEB_URL, contentModel.webUrl);
            contentValue.put(ContentEntity.COLUMN_HEADLINE, contentModel.fields.headline);
            contentValue.put(ContentEntity.COLUMN_STANDFIRST, contentModel.fields.standfirst);
            contentValue.put(ContentEntity.COLUMN_BODY, contentModel.fields.body);
            contentValue.put(ContentEntity.COLUMN_BYLINE, contentModel.fields.byline);
            contentValue.put(ContentEntity.COLUMN_THUMBNAIL, contentModel.fields.thumbnail);
            contentValue.put(ContentEntity.COLUMN_WORD_COUNT, contentModel.fields.wordcount);
        }
        return contentValues;
    }
}
