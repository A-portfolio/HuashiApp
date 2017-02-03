package net.muxi.huashiapp.util;

import android.widget.Toast;

import net.muxi.huashiapp.App;

/**
 * Created by ybao on 16/4/19.
 * Toast工具类
 */
public class ToastUtil {

    public static void showLong(String message){
        Toast.makeText(App.getContext(), message, Toast.LENGTH_LONG).show();
    }

    public static void showShort(String message){
        Toast.makeText(App.getContext(),message,Toast.LENGTH_SHORT).show();
    }

}
