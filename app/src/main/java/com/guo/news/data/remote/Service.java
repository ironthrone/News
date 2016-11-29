package com.guo.news.data.remote;


import com.guo.news.data.model.CommentModel;
import com.guo.news.data.model.ContentModel;
import com.guo.news.data.model.ResponseModel;
import com.guo.news.data.model.SectionModel;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/9/25.
 */
public interface Service {


    @GET("sections")
    Observable<ResponseModel<List<SectionModel>>> getSectionList();

    /**
     *
     * @param section
     * @param page
     * @param pageSize
     * @param fromDate "yyyy-MM-dd"
     * @param toDate
     * @return
     */
    @GET("search?show-fields=all")
    Observable<ResponseModel<List<ContentModel>>> getContentFromSection(@Query("section") String section,
                                                                        @Query("page") Integer page,
                                                                        @Query("page-size") Integer pageSize,
                                                                        @Query("from-date") String fromDate,
                                                                        @Query("to-date") String toDate);


    @POST("https://get20000.herokuapp.com/comment/list")
    @FormUrlEncoded
    Observable<ResponseModel<List<CommentModel>>> getCommentList(@Field("contentId") String contentId,
                                                                 @Field("page") Integer page);

    @POST("https://get20000.herokuapp.com/comment/add")
    @FormUrlEncoded
    Observable<ResponseModel<CommentModel>> addComment(@Field("contentId") String contentId,
                                                       @Field("content") String content);
}
