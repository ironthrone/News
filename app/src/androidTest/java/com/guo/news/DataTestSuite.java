package com.guo.news;

import com.guo.news.data.NewsDBTest;
import com.guo.news.data.NewsProviderTest;
import com.guo.news.data.UriMatcherTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Administrator on 2016/11/9.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({NewsDBTest.class, UriMatcherTest.class, NewsProviderTest.class})
public class DataTestSuite {
}
