package com.guo.news;

import android.support.test.runner.AndroidJUnit4;

import com.guo.news.data.model.SectionModel;
import com.guo.news.data.remote.ResultTransformer;
import com.guo.news.data.remote.ServiceHost;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import rx.Observer;

import static org.junit.Assert.assertTrue;

/**
 * Created by Administrator on 2016/11/15.
 */
@RunWith(AndroidJUnit4.class)
public class RestServiceTest {


    @Before
    public void setUp() {

    }
    @Test
    public void pullSections() {

        ServiceHost.getService().getSectionList()
                .map(new ResultTransformer<List<SectionModel>>())
                .subscribe(new Observer<List<SectionModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        assertTrue("pull fail", false);
                    }

                    @Override
                    public void onNext(List<SectionModel> sectionModels) {
                        assertTrue("pull success",true);

                    }
                });
    }
}
