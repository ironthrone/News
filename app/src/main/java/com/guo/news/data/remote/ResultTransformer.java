package com.guo.news.data.remote;

import com.guo.news.data.model.ResultModel;

import rx.functions.Func1;

/**
 * Created by Administrator on 2016/11/13.
 */
public class ResultTransformer<T> implements Func1<ResultModel<T>, T> {

    @Override
    public T call(ResultModel<T> tResultModel) {
        return tResultModel.results;
    }
}
