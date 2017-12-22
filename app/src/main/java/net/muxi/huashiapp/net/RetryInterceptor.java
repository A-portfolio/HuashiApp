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
        //不同的api规定的不一样
        if (responseCode == 401||responseCode==403) {
            PreferenceUtil.clearString(PreferenceUtil.BIG_SERVER_POOL);
            PreferenceUtil.clearString(PreferenceUtil.JSESSIONID);
            String sid = PreferenceUtil.getString(PreferenceUtil.STUDENT_ID);
            String pwd = PreferenceUtil.getString(PreferenceUtil.STUDENT_PWD);
            User user = new User();
            user.setSid(sid);
            user.setPassword(pwd);
            RxBus.getDefault().send(new RefreshSessionEvent(user));
            Log.d("send", "intercept: ");
        }
        return response;
    }
}
