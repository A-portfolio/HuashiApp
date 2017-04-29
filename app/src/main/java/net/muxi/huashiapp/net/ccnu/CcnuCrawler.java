package net.muxi.huashiapp.net.ccnu;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.common.data.InfoCookie;
import net.muxi.huashiapp.util.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/4/24
 */

public class CcnuCrawler {

    public static List<List<Cookie>> cookieStore = new ArrayList<>();
    public static CcnuService retrofitService;

    public static final String LOGIN_INFO = "http://portal.ccnu.edu.cn/loginAction.do";
    public static final String LINK_URL1 = "http://portal.ccnu.edu.cn/roamingAction.do?appId=XK";
    public static final String LINK_REDIRECT =
            "http://122.204.187.6/hzsflogin?ticket=wKhQEg0HHcVxKGGGWPFBB8OZO66BPUZU5NUE";

    public static final String LINK_URL2 = "http://122.204.187.6/xtgl/login_tickitLogin.html";

    public static final String COOKIE_KEY_BIG = "BIGipServerpool_jwc_xk";
    public static final String COOKIE_KEY_JSE = "JSESSIONID";

    static {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        CookieJar cookieJar = new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                for (Cookie cookie : cookies) {
                    Logger.d(String.format("<-- save: %s=%s domain: %s path: %s,url:%s ",
                            cookie.name(), cookie.value(), cookie.domain(), cookie.path(),
                            url.url().toString()));
                }
                cookieStore.add(cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                Logger.d("--> " + url.url().toString());
                List<Cookie> list = new ArrayList<>();
                switch (url.url().toString()) {
                    case LOGIN_INFO:
                        break;
                    case LINK_URL1:
                        list.addAll(cookieStore.get(0));
                        break;
                    case LINK_URL2:
                        list.addAll(cookieStore.get(1));
                        break;
                }
                if (list.size() == 0) {
                    Logger.d("list is empty url= " + url.url().toString());
                    return new ArrayList<>();
                }
                Cookie unSafeCookie = null;
                if (url.url().toString().equals(LINK_URL1)) {
                    unSafeCookie = new Cookie.Builder()
                            .name("unsafe")
                            .value("True")
                            .domain("portal.ccnu.edu.cn")
                            .path("/")
                            .build();
                    list.add(unSafeCookie);
                } else if (url.url().toString().equals(LINK_URL2)) {
                    unSafeCookie = new Cookie.Builder()
                            .name("unsafe")
                            .value("True")
                            .domain("122.204.187.6")
                            .path("/xtgl")
                            .build();
                    list.add(unSafeCookie);
                }

                for (Cookie cookie : list) {
                    System.out.println(String.format("%s=%s url=%s", cookie.name(), cookie.value(),
                            url.url().toString()));
                }
                return list;
            }
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cookieJar(cookieJar)
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://ccnubox.muxixyz.com/api/")
                .build();
        retrofitService = retrofit.create(CcnuService.class);

    }

    public static InfoCookie getInfoCookie() {
        try {
            if (!loginInfo(App.sUser.sid, App.sUser.password)) {
                return null;
            }
            retrofitService.updateCookie().execute();
            String big = "";
            String jse = "";
            for (Cookie cookie : cookieStore.get(1)) {
                if (cookie.name().equals(COOKIE_KEY_BIG)) {
                    big = cookie.value();
                }
            }
            for (Cookie cookie : cookieStore.get(2)) {
                if (cookie.name().equals(COOKIE_KEY_JSE)) {
                    jse = cookie.value();
                }
            }
            Logger.d("big: " + big);
            Logger.d("jse: " + jse);

            InfoCookie infoCookie = new InfoCookie(big, jse);
            return infoCookie;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean loginInfo(String sid, String password) {
        try {
            Response<ResponseBody> response = retrofitService.loginInfo(sid,
                    password).execute();
            if (response.body().string().contains("index_jg.jsp")) {
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
