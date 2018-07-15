package com.muxistudio.common.util;

import android.widget.Toast;

import com.muxistudio.common.base.Global;

/**
 * Created by ybao on 16/4/19.
 * Toast工具类
 */
public class ToastUtil {

    public static void showLong(String message){
        Toast.makeText(Global.getApplication(), message, Toast.LENGTH_LONG).show();
    }

    public static void showShort(String message){
        Toast.makeText(Global.getApplication(),message,Toast.LENGTH_SHORT).show();
    }

    public static void showShort(int rid){
        Toast.makeText(Global.getApplication(),Global.getApplication().getResources().getString(rid)
                ,Toast.LENGTH_SHORT).show();
    }

    public static void showLong(int rid){
        Toast.makeText(Global.getApplication(),Global.getApplication().getResources().getString(rid)
                ,Toast.LENGTH_LONG).show();
    }


}
