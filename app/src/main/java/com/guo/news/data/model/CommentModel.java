package com.guo.news.data.model;

import com.guo.news.data.remote.Service;
import com.guo.news.data.remote.ServiceHost;

import java.util.ArrayList;

import retrofit2.Callback;

/**
 * Created by Administrator on 2016/9/25.
 */
public class CommentModel {
    public int id;
    public int news_id;
    public long date;
    public String content;


    public static void getCommentList(Callback callback) {
        Service service = ServiceHost.getService();
        service.getCommentList().enqueue(new TheCallback<ArrayList<CommentModel>>() {
            @Override
            public void success(ArrayList<CommentModel> data) {

            }

            @Override
            public void fail(String msg) {

            }
        });
    }
}
