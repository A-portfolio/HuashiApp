package net.muxi.huashiapp.net;

import net.muxi.huashiapp.RxBus;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.event.RefreshSessionEvent;
import net.muxi.huashiapp.util.DateUtil;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.PreferenceUtil;

import java.io.IOException;
import java.util.Date;

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
        //只有在session过期的情况下才可以进行重试: if(url.contains("https://ccnubox.muxixyz.com/api/"))

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
            if(url.contains("grade")) {
                //查学分: 如果用户切换了学年,这个时候返回的值是403,但是这个时候不应该做处理!
                // https://ccnubox.muxixyz.com/api/grade/?xnm=2018&xqm=3
                String p = " https://ccnubox.muxixyz.com/api/grade/?xnm=";
                int curYear = Integer.parseInt(DateUtil.getCurYear(new Date(System.currentTimeMillis())));
                int queYear = Integer.parseInt(url.substring(p.length() - 1, p.length() + 3));
                Logger.d("current year "+curYear+" que year "+queYear   );
                if (curYear >= queYear) {
                    return 0;
                }
                if(curYear<queYear){
                    return 0;
                }
                return 403;
            }
            if(url.contains("table")){
                return 500;
            }

            return 0;
    }
}
