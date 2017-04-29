package net.muxi.huashiapp.net;

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
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new CookieInterceptor())
                .addInterceptor(new AuthorizationInterceptor())
                .readTimeout(15,TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15,TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("https://ccnubox.muxixyz.com/api/")
                .build();
        mRetrofitService = retrofit.create(RetrofitService.class);
    }

    public RetrofitService getRetrofitService(){
        return mRetrofitService;
    }
}
