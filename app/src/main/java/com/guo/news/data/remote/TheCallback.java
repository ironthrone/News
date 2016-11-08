package com.guo.news.data.remote;

import android.util.Log;

import com.guo.news.data.model.ResultModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/7/31.
 */
public abstract class TheCallback<T> implements Callback<ResultModel<T>> {

    private static final String TAG = TheCallback.class.getSimpleName();


    @Override
    public void onResponse(Call<ResultModel<T>> call, Response<ResultModel<T>> response) {
        Log.d(TAG, "success");
        if(response.isSuccessful()){

        if(response.body().showapi_res_code == 0){
            success(response.body().showapi_res_body);
        }else {
            fail(response.body().showapi_res_error);
        }
        }else {
            fail(response.message());
        }
    }

    @Override
    public void onFailure(Call<ResultModel<T>> call, Throwable t) {
        Log.d(TAG, "fail");

        fail(t.getMessage());
    }

    public abstract  void success(T data);
    public abstract  void fail(String msg);

}
