package com.guo.news.data.model;

/**
 * Created by Administrator on 2016/9/25.
 */
public class ResponseModel<T> {
    public Response<T> response;

    public static class Response<T> {

        public String status;
        public String message;
        public T results;
    }
}
