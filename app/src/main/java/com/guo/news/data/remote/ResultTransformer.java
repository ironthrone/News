package com.guo.news.data.remote;

import com.guo.news.data.model.ResponseModel;

import rx.functions.Func1;

/**
 * Created by Administrator on 2016/11/13.
 */
public class ResultTransformer<T> implements Func1<ResponseModel<T>, T> {

    @Override
    public T call(ResponseModel<T> responseModel) {
        return responseModel.response.results;
    }
}
