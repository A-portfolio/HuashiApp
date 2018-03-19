package com.muxistudio.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.muxistudio.common.base.Global;


/**
 * Created by ybao on 16/4/19.
 * 获取网络状态
 */
public class NetStatus {

    private static NetworkInfo getNetworkInfo(){
        ConnectivityManager cm = (ConnectivityManager) Global.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    //设备网路是否连接
    public static boolean isConnected(){
        NetworkInfo netInfo = getNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    //当前是否处于 wifi 状态
    public static boolean isWifi(){
        NetworkInfo netInfo = getNetworkInfo();
        return netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }
}
