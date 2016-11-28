package com.guo.news.data.remote;

import com.guo.news.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/7/31.
 */
public class ServiceHost {
    public static final String BASE_URL = "http://content.guardianapis.com/";

    public static final String API_KEY = BuildConfig.GUARDIAN_API_KEY;
    private static OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

    /**
     * add api-key for all request
     */
    static {
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request origin = chain.request();
                String host = origin.url().host();
                if (!host.contains("herokuapp")) {

                    HttpUrl url = origin.url().newBuilder().addQueryParameter("api-key", API_KEY)
                            .build();
                    origin = origin.newBuilder().url(url).build();
                }
                return chain.proceed(origin);
            }
        });
        HttpLoggingInterceptor.Level logLevel = BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE;
        clientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(logLevel));
    }

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(BASE_URL)

            .client(clientBuilder.build())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
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
