package net.muxi.huashiapp.net;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.common.data.InfoCookie;
import net.muxi.huashiapp.net.ccnu.CcnuCrawler;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/4/24
 */

public class CookieInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originRequest = chain.request();
        Request.Builder builder = originRequest.newBuilder();

        List<String> pathSegments = originRequest.url().pathSegments();

        if (pathSegments.get(1).equals("table") || pathSegments.get(1).equals("grade")) {
            if (CcnuCrawler.sSid != null && !CcnuCrawler.sSid.equals(App.sUser.sid)) {
                CcnuCrawler.clearCookieStore();
            }
            InfoCookie cookie = CcnuCrawler.getInfoCookie();
            if (cookie == null) {
                throw new IOException("cookie is null");
            }
            builder.addHeader("Bigipserverpool", cookie.Bigipserverpool_Jwc_Xk);
            builder.addHeader("Jsessionid", cookie.Jsessionid);
            builder.addHeader("Sid", App.sUser.sid);
        }

        Response response = chain.proceed(builder.build());
        return response;
    }

}
