package net.muxi.huashiapp.net.ccnu;

import net.muxi.huashiapp.common.data.InfoCookie;
import net.muxi.huashiapp.util.PreferenceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Cookie;
import okhttp3.CookieJar;
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
    private static Cookie accountJid=null,casPrivacy=null,casTgc = null,phpSessidLib =null;
    private static String location1 = "";
    private static String valueOfLt, valueOfExe;
    //在这个callback中拿到相关信息 valueoflt valueofexe
    private static CcnuService2 mCcnuService;
    private static String JSESSIONID_LOGIN_IN = null;
//    private static HashMap<HttpUrl,List<Cookie>> cookieMap = new HashMap<>();
    private static List<Cookie> cookieStore = new ArrayList<>();
    private static String LIB_URL = "http://202.114.34.15/reader/hwthau.php";
    //初始登录时候暂时缓存一下cookie
    //主要的cookiejar存放重要的信息

        public static CookieJar cookieJar = new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.addAll(cookies);
//                cookieMap.put(url,cookies);
                for (int i=0;i<cookies.size();i++) {
                    if (cookies.get(i).domain().equals("account.ccnu.edu.cn")) {
                        if (cookies.get(i).name().equals("JSESSIONID")) {
                            JSESSIONID_LOGIN_IN = cookies.get(i).value();
                            accountJid = cookies.get(i);
                        }
                        if (cookies.get(i).name().equals("CASPRIVACY")) {
                            casPrivacy = cookies.get(i);
                        }
                        if (cookies.get(i).name().equals("CASTGC")) {
                            casTgc = cookies.get(i);
                        }
                    }
                        //读取图书馆第一次登录的phpsessid
                    if(url.toString().equals("http://202.114.34.15/reader/hwthau.php")&&phpSessidLib==null){
                            phpSessidLib = cookies.get(i);
                    }
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                if(url.toString().equals("https://account.ccnu.edu.cn/cas/login?service=http%3A%2F%2F202.114.34.15%2Freader%2Fhwthau.php")) {
                    List<Cookie> list = new ArrayList<>();
                    list.add(casPrivacy);
                    list.add(casTgc);
                    list.add(accountJid);
                    return list;
                }
                if(url.toString().contains("http://202.114.34.15/reader/hwthau.php?ticket")){
                    List<Cookie> list= new ArrayList<>();
                    list.add(phpSessidLib);
                    return  list;
                }
                if(url.toString().equals("http://202.114.34.15/reader/hwthau.php")) {
                    List<Cookie> list = new ArrayList<>();
                    for (int i = 0; i < cookieStore.size(); i++)
                        if (cookieStore.get(i).value().contains("ST-") && cookieStore.get(i).value().contains("accountccnueducn")) {
                            list.add(cookieStore.get(i));
                            if(cookieStore.get(i).value()!=null||cookieStore.get(i).value().equals(""))
                                PreferenceUtil.saveString(PreferenceUtil.PHPSESSID,cookieStore.get(i).value());
                            return list;
                        }
                }
                if(url.toString().equals("http://202.114.34.15/reader/redr_info.php")){
                    List<Cookie> list = new ArrayList<>();
                    for (int i = 0; i < cookieStore.size(); i++)
                        if (cookieStore.get(i).value().contains("ST-") && cookieStore.get(i).value().contains("accountccnueducn")) {
                            list.add(cookieStore.get(i));
                            return list;
                        }
                }
                return cookieStore;
            }
        };

    public static void initCrawler() {
        //此处的内容详细参考这里:
        //https://www.zybuluo.com/Humbert/note/970726
        //登录分为三个步骤1.登录信息门户(one.ccnu.edu.cn) 2.登录教务系统 3.登录图书馆系统
        //initCrawler()的目的是为了登录 信息门户并且获取三个重要的cookie: JSESSIONID(domain:account.ccnu.edu) CASPRIVACY CASTGC
        //信息门户登录分为两步:第一步获取JSESSIONID(domain:account.ccnu.edu) 第二步是获取CASPRIVACY 和 CASTGC
        //所有的cookie都维护在cookieJar中
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cookieJar(cookieJar)
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
//                .addConverterFactory(GsonConverterFactory.create())
                .client(client2)
                .baseUrl("https://ccnubox.muxixyz.com/api/")
                .build();
        //完成step2 的CASPRIVACY的获取
        mCcnuService = retrofit.create(CcnuService2.class);
    }

    public static boolean performLogin(String username, String userpassword) throws IOException {
        initCrawler();
        retrofit2.Response<ResponseBody> responseBody = mCcnuService.performCampusLogin
                (JSESSIONID_LOGIN_IN, username, userpassword,
                        valueOfLt, valueOfExe, "submit", "LOGIN").execute();

        retrofit2.Response<ResponseBody> responseBody2 = mCcnuService.performSystemLogin().execute();
        String bigResponse1 = responseBody.body().string();
        performLibLogin();
        //三重验证确保登录成功
        return isSuccessful();
    }

    private static boolean performLibLogin() throws IOException {
        //整个步骤拆分成三个部分:获取第一个phpsessionid
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(25, TimeUnit.SECONDS)
                .connectTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25,TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .cookieJar(cookieJar).build();
        //step1
        Request request0 = initRequestBuilder()
                .url("http://202.114.34.15/reader/hwthau.php")
                .get()
                .build();
        client.newCall(request0).execute();
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
        String str1 = "((.*)(name=\"lt\" value=\")(.*)(\" />))";
        String str2 = "((.*)(name=\"execution\" value=\")(.*)(\" />))";
        //<input type="hidden" name="lt" value="LT-31315-O4Nt1gZeHUSnmzr4DALQwyn3xNyir6-account.ccnu.edu.cn" />
        //<input type="hidden" name="execution" value="e1s1" />
        String bigString = responseBody;
        Pattern r = Pattern.compile(str1);
        Matcher m = r.matcher(bigString);
        Pattern r2 = Pattern.compile(str2);
        Matcher m2 = r2.matcher(bigString);

        while (m.find()) {
            valueOfLt = m.group(4);
        }


        while (m2.find()) {
            valueOfExe = m2.group(4);
        }
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

//    private static void storeLocation(retrofit2.Response<ResponseBody> response) {
    public static void clearCookieStore(){
        cookieStore.clear();
    }


    private static boolean isSuccessful(){
        boolean flag1 = false,flag2 = true, flag3 = false;
        for(int i=0;i <cookieStore.size();i++){
            if(cookieStore.get(i).name().equals("CASPRIVACY")) {
                flag1 = true;
            }
//            if(!PreferenceUtil.getString(PreferenceUtil.JSESSIONID).equals("")){
//                flag2 = true;
//            }
            if(!PreferenceUtil.getString(PreferenceUtil.PHPSESSID).equals("")){
                flag3 =true;
            }
        }
        if(flag1&&flag2&&flag3){
           // cookieStore.clear();
            return true;
        }else{
            cookieStore.clear();
            return false;
        }
    }
}
