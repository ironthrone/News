package com.guo.news.data.remote;


import com.guo.news.data.model.CommentModel;
import com.guo.news.data.model.ContentModel;
import com.guo.news.data.model.ResultModel;
import com.guo.news.data.model.SectionModel;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/9/25.
 */
public interface Service {


    @GET("section")
    Observable<ResultModel<List<SectionModel>>> getSectionList();

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
    Observable<ResultModel<List<ContentModel>>> getContentFromSection(@Query("section") String section,
                                                                      @Query("page") Integer page,
                                                                      @Query("page-size") Integer pageSize,
                                                                      @Query("from-date") String fromDate,
                                                                      @Query("to-date") String toDate);


    @GET("comment")
    Observable<ResultModel<List<CommentModel>>> getCommentList();

    @POST("comment/add")
    Observable<ResultModel<String>> addComment(@Body CommentModel co);
}
