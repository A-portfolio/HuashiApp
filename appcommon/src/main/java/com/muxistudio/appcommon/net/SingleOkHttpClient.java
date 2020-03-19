package com.muxistudio.appcommon.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class SingleOkHttpClient {
    private OkHttpClient client;
    private SingleOkHttpClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

         client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }
    public static OkHttpClient getClient(){
        return Holder.INSTANCE.client;
    }

    private static class Holder{
        private static SingleOkHttpClient INSTANCE=new SingleOkHttpClient();

    }

}
