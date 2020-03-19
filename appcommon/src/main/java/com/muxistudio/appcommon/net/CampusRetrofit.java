package com.muxistudio.appcommon.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ybao on 16/4/28.
 * retrofit初始化封装
 */
public class CampusRetrofit{

    private final RetrofitService mRetrofitService;

    public CampusRetrofit() {

        OkHttpClient client = SingleOkHttpClient.getClient().newBuilder()
                .addInterceptor(new TimeOutChangeInterceptor("https://ccnubox.muxixyz.com/api/ios/config/"))
                .addInterceptor(new CookieInterceptor())
                .addInterceptor(new AuthorizationInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()

                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("https://ccnubox.muxixyz.com/api/")
                //http://39.108.79.110:5678/api/
                .build();
        mRetrofitService = retrofit.create(RetrofitService.class);

    }

    public RetrofitService getRetrofitService(){
        return mRetrofitService;
    }

}
