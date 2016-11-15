package com.guo.news.data.model;

/**
 * Created by Administrator on 2016/9/25.
 */
public class ContentModel {
    public String id;
    public String sectionId;
    public String sectionName;
    public String webPublicationDate;
    public String webUrl;

    public FieldModel fields;

    public static class FieldModel {
        public String headline;
        public String trailText;
        public String byline;
        public String body;
        public String thumbnail;
        public int wordcount;
    }
}

