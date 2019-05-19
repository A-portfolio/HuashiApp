package net.muxi.huashiapp.login;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MyCookieJar implements CookieJar {

    public static final String TAG="cookie";
    private CookieManager manager;

    public MyCookieJar(Context context){
        manager=new CookieManager(context);
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        Log.i(TAG, "saveFromResponse:  "+url.host());
        manager.addAll(url.host(),cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        Log.i(TAG, "loadForRequest:  "+url.host());

        return manager.provideCookies(url.host());
    }

    //把cookie保存到本地
    //在每次请求结束是调用
    public void saveCookieToLocal(){
        manager.saveToPre();
    }
}
