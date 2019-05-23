package net.muxi.huashiapp.login;

import android.content.Context;
import android.util.Log;

import com.muxistudio.common.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MyCookieJar implements CookieJar {

    public static final String TAG = "cookie";
    private CookieManager manager;

    public MyCookieJar(Context context) {
        manager = new CookieManager(context);
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        Log.i(TAG, "saveFromResponse:  host   " + url.host());
        manager.addAll(url.host(), cookies);


        //这个是为了和之前的请求课表的方法兼容，而增加的，未来后端会改进
        for (int i = 0; i < cookies.size(); i++) {
            Log.i(TAG, "saveFromResponse: " + cookies.get(i).name() + "  " + cookies.get(i).value());
            Cookie cookie = cookies.get(i);
            if (cookie.name().equals("BIGipServerpool_jwc_xk")) {
                PreferenceUtil.saveString(PreferenceUtil.BIG_SERVER_POOL, cookie.value());
                continue;
            }
            if (cookie.name().equals("JSESSIONID") && url.host().contains("xk.ccnu.edu.cn")) {
                PreferenceUtil.saveString(PreferenceUtil.JSESSIONID, cookie.value());
            }


        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        Log.i(TAG, "loadForRequest:  " + url.host());
        List<Cookie> cookies = manager.provideCookies(url.host());
        Log.i(TAG, "loadForRequest: " + cookies.size());

        if (url.host().equals("202.114.34.15")) {
            for (int i = 0; i < cookies.size(); i++) {
                if (cookies.get(i).value().contains("ST-")) {
                    Log.i(TAG, "loadForRequest: get  st");
                    List<Cookie> temp = new ArrayList<>();
                    temp.add(cookies.get(i));
                    return temp;
                }
            }

        }

        for (int i = 0; i < cookies.size(); i++) {
            Log.i(TAG, "loadForRequest: " + cookies.get(i).name() + "  " + cookies.get(i).value());
        }

        //专门对图书馆登录的处理，否则会无限循环重定向，原因是有两个PHPSESSID,他只会读取第一个，造成无限重定向循环
        //真不知道该怎么处理了...

        return cookies;
    }

    //把cookie保存到本地
    //在每次请求结束是调用
    public void saveCookieToLocal() {
        manager.saveToPre();
    }

    public void clearCookie() {
        manager.clearAll();
    }

    //如果登录成功请用这个
    public void useOldAccountCookie() {

        manager.useOldAccountCookie();
    }
}
