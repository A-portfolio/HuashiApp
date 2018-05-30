package net.muxi.huashiapp.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import net.muxi.huashiapp.App;

public class NetUtil {

    public static String getWifiSSID(){
        WifiManager manager = (WifiManager) App.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = manager.getConnectionInfo();
        return wifiInfo.getSSID();
    }
}
