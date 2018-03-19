package com.muxistudio.appcommon.net;

import com.muxistudio.appcommon.data.InfoCookie;
import com.muxistudio.appcommon.net.ccnu.CcnuCrawler;
import com.muxistudio.appcommon.net.ccnu.CcnuCrawler2;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.appcommon.utils.Base64Util;
import com.muxistudio.common.util.PreferenceUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/4/24
 */



//这个类的用法非常的tricky 也可能是我水平太差的原因(kolibreath) 这个在真正需要保存cookie的地方 在教务系统的请求中并没有
//保存cookie，没有在那个OkhttpClient中携带上这个截断器， 而是在cookieStore（cookieJar）中临时储存（内存），在之后的一个请求中
//携带这个截断器在设置cookie到header的同时保存cookie
    //网络请求步骤：信息门户 和教务系统https://www.zybuluo.com/Humbert/note/970726
    //图书馆 ：https://www.zybuluo.com/Humbert/note/1032966
public class CookieInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originRequest = chain.request();
        Request.Builder builder = originRequest.newBuilder();

        List<String> pathSegments = originRequest.url().pathSegments();

        if (pathSegments.get(1).equals("table") || pathSegments.get(1).equals("grade")) {
            if (CcnuCrawler.sSid != null && !CcnuCrawler.sSid.equals(UserAccountManager.getInstance().getInfoUser().sid)) {
                CcnuCrawler.clearCookieStore();
            }
            //执行了储存
            InfoCookie cookie = CcnuCrawler2.getInfoCookie();
            String big = PreferenceUtil.getString(PreferenceUtil.BIG_SERVER_POOL);
            String jid = PreferenceUtil.getString(PreferenceUtil.JSESSIONID);
            if (big.equals("") && jid.equals("")) {
                builder.addHeader("Bigipserverpool", cookie.Bigipserverpool_Jwc_Xk);
                builder.addHeader("Jsessionid", cookie.Jsessionid);
                builder.addHeader("Sid", UserAccountManager.getInstance().getInfoUser().sid);
                builder.addHeader("Authorization", Base64Util.createBaseStr(UserAccountManager.getInstance().getInfoUser()));
            } else {
                builder.addHeader("Bigipserverpool", big);
                builder.addHeader("Jsessionid", jid);
                builder.addHeader("Sid", UserAccountManager.getInstance().getInfoUser().sid);
                builder.addHeader("Authorization", Base64Util.createBaseStr(UserAccountManager.getInstance().getInfoUser()));
            }
        }

        return chain.proceed(builder.build());
    }
}