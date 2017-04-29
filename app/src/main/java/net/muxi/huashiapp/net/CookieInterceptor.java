package net.muxi.huashiapp.net;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.common.data.InfoCookie;
import net.muxi.huashiapp.common.data.Scores;
import net.muxi.huashiapp.net.ccnu.CcnuCrawler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        if (pathSegments.get(1).equals("table") || pathSegments.get(1).equals("grade")){
            InfoCookie cookie = CcnuCrawler.getInfoCookie();
            if (cookie == null){
                throw new IOException("cookie is null");
            }
            builder.addHeader("Bigipserverpool_Jwc_Xk",cookie.Bigipserverpool_Jwc_Xk);
            builder.addHeader("Jsessionid",cookie.Jsessionid);
            builder.addHeader("Sid", App.sUser.sid);
        }

        Response response = chain.proceed(builder.build());
        return response;
    }

}
