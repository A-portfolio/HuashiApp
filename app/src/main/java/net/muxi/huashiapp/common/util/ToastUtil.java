package net.muxi.huashiapp.common.util;

import android.widget.Toast;

/**
 * Created by ybao on 16/4/19.
 */
public class ToastUtil {

    public static void showLong(String message){
        Toast.makeText(App.getContext(), message, Toast.LENGTH_LONG).show();
    }

    public static void showShort(String message){
        Toast.makeText(App.getContext(),message,Toast.LENGTH_SHORT).show();
    }

}
