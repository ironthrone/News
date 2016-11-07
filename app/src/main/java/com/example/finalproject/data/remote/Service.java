package com.example.finalproject.data.remote;

import com.example.finalproject.data.model.ChannelListModel;
import com.example.finalproject.data.model.CommentModel;
import com.example.finalproject.data.model.PageModel;
import com.example.finalproject.data.model.ResultModel;

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

<<<<<<< HEAD
    @GET(ServiceConstant.CHANNEL_PATH)
    Call<ResultModel<ChannelListModel>> getChannelList();

    @FormUrlEncoded
    @POST(ServiceConstant.NEWS_PATH)
=======
    @GET(RestConstant.CHANNEL_PATH)
    Call<ResultModel<ChannelListModel>> getChannelList();

    @FormUrlEncoded
    @POST(RestConstant.NEWS_PATH)
>>>>>>> 7a0cf304b6fdf772dcbd00efaf252e2b09cb1f50
    Call<ResultModel<PageModel>> getNewsFromChannel(@Field("channelId") String channelId,
                                                    @Field("page") Integer page,
                                                    @Field("maxResult") Integer maxItem);


    @GET("comment")
    Call<ResultModel<ArrayList<CommentModel>>> getCommentList();

    @POST("comment/add")
    Call<ResultModel<String>> addComment(@Body CommentModel co);
}
