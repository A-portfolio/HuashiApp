package net.muxi.huashiapp.net.ccnu;

import android.util.Log;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.common.data.InfoCookie;
import net.muxi.huashiapp.util.PreferenceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by kolibreath on 17-12-10.
 */

/*
现在的情况和之前不同，然后由于和教务系统相关联的有学分和课表 所以不仅要登录进入校园系统 还需要登录录入选课系统
 */
public class CcnuCrawler2 {
    private static String Location = "";
    private static String valueOfLt, valueOfExe;
    //在这个callback中拿到相关信息 valueoflt valueofexe
    private static CcnuService2 mCcnuService;
    private static String JSESSIONID_LOGIN_IN = null;
    private static List<Cookie> cookieStore = new ArrayList<>();
    //初始登录时候暂时缓存一下cookie
    //主要的cookiejar存放重要的信息

    public static CookieJar cookieJar = new CookieJar() {
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookieStore.addAll(cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
//            Log.d("herecookie", "saveFromResponse: " + cookieStore.toString());
            return cookieStore;
        }
    };

    public static void initCrawler() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cookieJar(new CookieJar() {
                    List<Cookie> cookies;

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        this.cookies = cookies;
                        for (int i = 0; i < cookies.size(); i++) {
                            if (cookies.get(i).name().equals("JSESSIONID")) {
                                JSESSIONID_LOGIN_IN = cookies.get(i).value();
                                break;
                            }
                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        if (cookies != null)
                            return cookies;
                        return new ArrayList<Cookie>();
                    }
                })
                .build();
        Request request = initRequestBuilder()
                .url("https://account.ccnu.edu.cn/cas/login")
                .get()
                .build();
        try {
            //这里会阻塞线程 直到拿到数据
            Response response = client.newCall(request).execute();
            getValue(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //将相关的cookie存放到cookieJar 以便于教务系统登录
        OkHttpClient client2 = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cookieJar(cookieJar)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client2)
                .baseUrl("https://ccnubox.muxixyz.com/api/")
                .build();
        mCcnuService = retrofit.create(CcnuService2.class);
    }

    /**
     * including two steps : first login in account.ccnu.edu.cn the login the student's affairs system
     * to get credit and scores and table
     *
     * @param username
     * @param userpassword
     * @return
     * @throws IOException
     */
    public static boolean performLogin(String username, String userpassword) throws IOException {
        Log.d("presenter", "performLogin: middle");
        initCrawler();
        retrofit2.Response<ResponseBody> responseBody = mCcnuService.performCampusLogin
                (JSESSIONID_LOGIN_IN, username, userpassword, valueOfLt, valueOfExe, "submit", "LOGIN").execute();

        retrofit2.Response<ResponseBody> responseBody2 = mCcnuService.performSystemLogin().execute();
        performLibLogin();
        return true;
    }

    private static boolean performLibLogin() throws IOException {
        //这个client需要截断，用以获取转发的location
        OkHttpClient client2 = new OkHttpClient.Builder()
                .followRedirects(false)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .cookieJar(cookieJar)
                .build();

        Request request = initRequestBuilder()
                .url("http://202.114.34.15/reader/redr_info.php")
                .get()
                .build();
        //获取phpsession1
        client2.newCall(request).execute();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client2)
                .baseUrl("https://ccnubox.muxixyz.com/api/")
                .build();
        CcnuService2 mCcnuService2 = retrofit.create(CcnuService2.class);
        retrofit2.Response<ResponseBody> responseBodyResponse = mCcnuService2.performLibLogin().execute();
        ;
        storeLocation(responseBodyResponse);
        Location = responseBodyResponse.headers().get("Location");

        Request request2 = initRequestBuilder()
                .url(Location)
                .get()
                .build();
        client2.newCall(request2).execute();
        return true;
    }

    //提取header的公用字段
    private static Request.Builder initRequestBuilder() {
        return new Request.Builder()
                .addHeader("accept", "text/html,application/xhtml+xml" +
                        ",application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .addHeader("accept-encoding", "gzip, deflate, br")
                .addHeader("accept-language", "en,zh-CN;q=0.9,zh;q=0.8")
                .addHeader("cache-control", "no-cache")
                .addHeader("connection", "keep-alive")
                .addHeader("pragma", "no-cache")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent",
                        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36")
                ;

    }

    //获取字段lt 和exe
    private static void getValue(String responseBody) {
        String str1 = "<input type=\"hidden\" name=\"lt\" value=\"(.*)\" />";
        String str2 = "<input type=\"hidden\" name=\"execution\" value=\"(.*)\" />";
        //<input type="hidden" name="lt" value="LT-31315-O4Nt1gZeHUSnmzr4DALQwyn3xNyir6-account.ccnu.edu.cn" />
        //<input type="hidden" name="execution" value="e1s1" />
        String bigString = responseBody;
        Pattern r = Pattern.compile(str1);
        Matcher m = r.matcher(bigString);
        Pattern r2 = Pattern.compile(str2);
        Matcher m2 = r2.matcher(bigString);

        String keyLine1 = null;
        String keyLine2 = null;

        while (m.find()) {
            keyLine1 = m.group();
        }
            while (m2.find()) {
                keyLine2 = m2.group();
        }
        valueOfLt = keyLine1.split("value=\"")[1].split("\" />")[0];
        valueOfExe = keyLine2.split("value=\"")[1].split("\" />")[0];
    }

    public static InfoCookie getInfoCookie() {
        InfoCookie infoCookie;
        String bigServerPool = "", jsession = "";
        //临时储存多个jsession 然后选出最后一个 作为jid_3
        List<String> tempJsessionList = new ArrayList<>();
        for (int i = 0; i < cookieStore.size(); i++) {
            if (cookieStore.get(i).name().equals("JSESSIONID")) {
                jsession = cookieStore.get(i).value();
                tempJsessionList.add(jsession);
            }
            if (cookieStore.get(i).name().equals("BIGipServerpool_jwc_xk")) {
                bigServerPool = cookieStore.get(i).value();
            }
            if (cookieStore.get(i).name().equals("PHPSESSID")) {
                PreferenceUtil.saveString(PreferenceUtil.PHPSESSION_ID,
                        cookieStore.get(i).value());
                App.PHPSESSID = cookieStore.get(i).value();
            }
        }
        if (!tempJsessionList.isEmpty()) {
            jsession = tempJsessionList.get(tempJsessionList.size() - 1);
            infoCookie = new InfoCookie(bigServerPool, jsession);
            //顺便保存/持久化一下
            saveCookies(bigServerPool, jsession);
        } else {
            bigServerPool = PreferenceUtil.getString(PreferenceUtil.BIG_SERVER_POOL);
            jsession = PreferenceUtil.getString(PreferenceUtil.JSESSIONID);
            infoCookie = new InfoCookie(bigServerPool, jsession);
        }
        return infoCookie;
    }

    private static void saveCookies(String big, String jid) {
        PreferenceUtil.saveString(PreferenceUtil.BIG_SERVER_POOL, big);
        PreferenceUtil.saveString(PreferenceUtil.JSESSIONID, jid);
    }

    private static void storeLocation(retrofit2.Response<ResponseBody> response) {
        Headers headers = response.headers();
        String location = headers.get("Location");
        String url = "http://202.114.34.15/reader/login.php?ticket=", apdix = "account.ccnu.edu.cn";
        try {
            String phpSessionId = location.substring(url.length(), location.length() - apdix.length()) + "accountccnueducn";
            PreferenceUtil.saveString(PreferenceUtil.PHPSESSION_ID, phpSessionId);
        }catch(Exception e){
            e.printStackTrace();
        }
//        Log.d("heaven", "storeLocation: " + phpSessionId);
    }

}
