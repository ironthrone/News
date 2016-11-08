package com.guo.news.data.remote;

import com.guo.news.data.model.ChannelListModel;
import com.guo.news.data.model.CommentModel;
import com.guo.news.data.model.PageModel;
import com.guo.news.data.model.ResultModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/9/25.
 */
public interface Service {

    @GET(ServiceHost.CHANNEL_PATH)
    Call<ResultModel<ChannelListModel>> getChannelList();

    @FormUrlEncoded
    @POST(ServiceHost.NEWS_PATH)
    Call<ResultModel<PageModel>> getNewsFromChannel(@Field("channelId") String channelId,
                                                    @Field("page") Integer page,
                                                    @Field("maxResult") Integer maxItem);


    @GET("comment")
    Call<ResultModel<ArrayList<CommentModel>>> getCommentList();

    @POST("comment/add")
    Call<ResultModel<String>> addComment(@Body CommentModel co);
}