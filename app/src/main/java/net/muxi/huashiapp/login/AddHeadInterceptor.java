package net.muxi.huashiapp.login;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddHeadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();
        if (request.url().toString().contains("ccnu.edu.cn")) {
            Log.i("head  intercept  url", request.url().toString()+"   intercept: load head for ccnu.edu.cn");
            request.newBuilder()
                    .addHeader("accept", "text/html,application/xhtml+xml" +
                            ",application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                    .addHeader("accept-encoding", "gzip, deflate, br")
                    .addHeader("accept-language", "en,zh-CN;q=0.9,zh;q=0.8")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("connection", "keep-alive")
                    .addHeader("pragma", "no-cache")
                    .addHeader("upgrade-insecure-requests", "1")
                    .addHeader("user-agent",
                            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36");

        }
        return chain.proceed(request);
    }
}
