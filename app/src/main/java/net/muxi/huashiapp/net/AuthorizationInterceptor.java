package net.muxi.huashiapp.net;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.util.Base64Util;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/4/29
 * 增加 base64 加密认证
 */

public class AuthorizationInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        if (!request.url().pathSegments().get(1).equals("info")) {
            builder = request.newBuilder();
            builder.header("Authorization", Base64Util.createBaseStr(App.sUser));
        }
        return chain.proceed(builder.build());
    }
}
