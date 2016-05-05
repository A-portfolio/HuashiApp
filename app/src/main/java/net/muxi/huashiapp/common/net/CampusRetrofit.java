package net.muxi.huashiapp.common.net;

import net.muxi.huashiapp.common.Api;

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
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Api.BASE_URL)
                .build();
        mRetrofitService = retrofit.create(RetrofitService.class);
    }

    public RetrofitService getRetrofitService(){
        return mRetrofitService;
    }
}
