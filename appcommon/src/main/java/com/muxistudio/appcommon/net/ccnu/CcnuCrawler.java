package com.muxistudio.appcommon.net.ccnu;

import com.muxistudio.appcommon.data.InfoCookie;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.common.util.Logger;

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

    private static List<List<Cookie>> cookieStore = new ArrayList<>();
    private static CcnuService retrofitService;
    public static String sSid;

    public static final String LOGIN_INFO = "http://portal.ccnu.edu.cn/loginAction.do";
    public static final String LINK_URL1 = "http://portal.ccnu.edu.cn/roamingAction.do?appId=XK";
    public static final String LINK_REDIRECT =
            "http://122.204.187.6/hzsflogin?ticket=wKhQEg0HHcVxKGGGWPFBB8OZO66BPUZU5NUE";

    public static final String LINK_URL2 = "http://122.204.187.6/xtgl/login_tickitLogin.html";

    public static final String COOKIE_KEY_BIG = "BIGipServerpool_jwc_xk";
    public static final String COOKIE_KEY_JSE = "JSESSIONID";

    //初始化爬虫代理,当用户切换账号时需要重新初始化
    public static void initCrawler(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //需要使用cookieJar来保存cookie
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
                    case LINK_REDIRECT:
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
                .cookieJar(cookieJar)
                .addInterceptor(interceptor)
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
        if (cookieStore.size() > 2) {
            return searchCookie();
        }

            if (!loginInfo(UserAccountManager.getInstance().getInfoUser().sid, UserAccountManager.getInstance().getInfoUser().password)) {
                return null;
            }
            InfoCookie infoCookie = searchCookie();
            sSid = UserAccountManager.getInstance().getInfoUser().sid;
            return infoCookie;
    }

    public static InfoCookie searchCookie() {
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
        InfoCookie infoCookie = new InfoCookie(big, jse);
        return infoCookie;
    }

    public static boolean loginInfo(String sid, String password) {
        clearCookieStore();
        initCrawler();
        try {
            //这个是一个okhttp的写法
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

    public static void clearCookieStore() {
        cookieStore.clear();
    }

}
