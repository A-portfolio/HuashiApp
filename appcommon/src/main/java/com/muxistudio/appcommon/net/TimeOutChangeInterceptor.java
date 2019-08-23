package com.muxistudio.appcommon.net;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TimeOutChangeInterceptor implements Interceptor {
    private String needChangeUrl;
    public TimeOutChangeInterceptor(String url){
        this.needChangeUrl=url;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();
        if (request.url().toString().equals(needChangeUrl)){
            return chain.withConnectTimeout(3, TimeUnit.SECONDS)
                    .withReadTimeout(3,TimeUnit.SECONDS)
                    .withWriteTimeout(3,TimeUnit.SECONDS)
                    .proceed(request);

        }

        return chain.proceed(request);
    }
}
