package net.muxi.huashiapp.common.util;

import android.util.Log;

import net.muxi.huashiapp.App;

/**
 * Created by ybao on 16/4/19.
 */
public class Logger {
    public static void d(String msg){
        Log.d("" + App.getContext(),msg);
    }
}

