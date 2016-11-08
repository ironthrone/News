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
public class ServiceHost {
    public static final String BASE_URL = "http://route.showapi.com/";
    public static final String CHANNEL_PATH = "109-34";
    public static final String NEWS_PATH = "109-35";

    public static final String APP_ID = "24838";
    public static final String API_SIGN = "72bf788bc587403b9095c8c5ba1b255c";
    private static OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

    static {
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request origin = chain.request();
                HttpUrl url = origin.url().newBuilder().addQueryParameter("showapi_appid", APP_ID)
                        .addQueryParameter("showapi_sign", API_SIGN).build();
                origin = origin.newBuilder().url(url).build();
                return chain.proceed(origin);
            }
        });
    }
    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
    private static Service mService;

    public static Service getService() {
        if (mService == null) {
        mService = retrofitBuilder.client(clientBuilder.build())
                .build().create(Service.class);

        }
        return mService;
    }

}
