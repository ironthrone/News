package com.example.finalproject.data.remote;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/7/31.
 */
public class ServiceGenerator {
    private static OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

    static {
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request origin = chain.request();
                HttpUrl url = origin.url().newBuilder().addQueryParameter("showapi_appid", RestConstant.APP_ID)
                        .addQueryParameter("showapi_sign", RestConstant.API_SIGN).build();
                origin = origin.newBuilder().url(url).build();
                return chain.proceed(origin);
            }
        });
    }
    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(RestConstant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S generate(Class<S> s){

        return retrofitBuilder.client(clientBuilder.build())
                .build().create(s);
    }

}
