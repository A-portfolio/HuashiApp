package net.muxi.huashiapp.net;

import android.util.Log;

import net.muxi.huashiapp.RxBus;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.event.RefreshSessionEvent;
import net.muxi.huashiapp.util.PreferenceUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kolibreath on 17-12-20.
 */

public class RetryInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        int responseCode = response.code();
        String url = request.url().toString();
        //每个api规定的不一样
        int code = getResponseCode(url);
        Log.d("responsecode", "intercept: "+code+" "+responseCode);
        //只有在session过期的情况下才可以进行重试: if(url.contains("https://ccnubox.muxixyz.com/api/"))
       // if(url.contains("lib")){
        if (responseCode==code){
            PreferenceUtil.clearString(PreferenceUtil.BIG_SERVER_POOL);
            PreferenceUtil.clearString(PreferenceUtil.JSESSIONID);
            String sid = PreferenceUtil.getString(PreferenceUtil.STUDENT_ID);
            String pwd = PreferenceUtil.getString(PreferenceUtil.STUDENT_PWD);
            User user = new User();
            user.setSid(sid);
            user.setPassword(pwd);
            RxBus.getDefault().send(new RefreshSessionEvent(user));
        }
        return response;
    }

    private int getResponseCode(String url){
            if(url.contains("lib")||url.contains("table")) {
                return 401;
            }
            if(url.contains("grade")){
                return 403;
            }
            if(url.contains("table")){
                return 500;
            }

            return 0;
    }
}
