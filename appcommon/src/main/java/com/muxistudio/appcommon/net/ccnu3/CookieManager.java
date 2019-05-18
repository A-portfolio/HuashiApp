package com.muxistudio.appcommon.net.ccnu3;

import android.content.Context;
import android.content.SharedPreferences;

import com.muxistudio.appcommon.utils.AppUtil;

import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;

public class CookieManager {

    private final SharedPreferences cookiePrefs;
    private static final String HOST_PRE="HOST_";
    private HashMap<String , List<Cookie>>cookies;
    private  static final String COOKIE_FILE="CookiePreFile";

    /**
     *
     * @param context 必须是application的context,用app类里的，千万别用当前activity的Context否则造成内存泄漏
     */
    public CookieManager(Context context) {
        this.cookiePrefs =context.getSharedPreferences(COOKIE_FILE,0);
    }



}
