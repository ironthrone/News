package com.guo.news.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import com.guo.news.data.local.NewsContract.CommentEntity;
import com.guo.news.data.local.NewsContract.ContentEntity;
import com.guo.news.data.local.NewsContract.SectionEntity;
import com.guo.news.data.local.NewsProvider;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Administrator on 2016/11/9.
 */
@RunWith(AndroidJUnit4.class)
public class UriMatcherTest {
    @Test
    public void matchContent() {
        UriMatcher matcher = NewsProvider.getUriMatcher();
        Uri contentUri = ContentEntity.CONTENT_URI;
        Uri contentWithSectionUri = ContentEntity.buildContentWithSectionUri("football");
        Uri contentWithIdUri = ContentEntity.buildContentWithIdUri("11");
        Uri sectionUri = SectionEntity.CONTENT_URI;
        Uri commentUri = CommentEntity.CONTENT_URI;
        Uri commentWithContentUri = CommentEntity.buildWithContentIDUrl("google");
        Assert.assertEquals("Content match error", NewsProvider.CONTENT,matcher.match(contentUri));
        Assert.assertEquals("ContentWithSection match error", NewsProvider.CONTENT_WITH_SECTION,matcher.match(contentWithSectionUri));
        Assert.assertEquals("ContentWithId match error", NewsProvider.CONTENT_WITH_ID,matcher.match(contentWithIdUri));
        Assert.assertEquals("Section match error", NewsProvider.SECTION,matcher.match(sectionUri));
        Assert.assertEquals("Comment match error", NewsProvider.COMMENT,matcher.match(commentUri));
        Assert.assertEquals("CommentWithContent match error", NewsProvider.COMMENT_WITH_CONTENT,matcher.match(commentWithContentUri));
    }
}
