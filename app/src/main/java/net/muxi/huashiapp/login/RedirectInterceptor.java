package net.muxi.huashiapp.login;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


//重定向拦截器
public class RedirectInterceptor implements Interceptor {
    private final static String TAG="MYINTERCEPTOR";
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request=chain.request();
        Log.i(TAG, "intercept: redirect url:-----> "+request.url().toString());

        return chain.proceed(request);
    }
}
