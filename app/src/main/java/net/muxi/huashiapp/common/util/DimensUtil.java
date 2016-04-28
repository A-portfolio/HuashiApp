package net.muxi.huashiapp.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.view.Display;
import android.view.WindowManager;

import net.muxi.huashiapp.App;

import java.lang.reflect.Field;

/**
 * Created by ybao on 16/4/20.
 * 获取一些控件或窗口的高宽
 */
public class DimensUtil {

    public static Context sContext = App.getContext();

    public static Display getDisplay() {
        WindowManager windowManager = (WindowManager) sContext.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay();
    }

    public static int getScreenWidth() {
        return getDisplay().getWidth();
    }

    public static int getScreenHeight() {
        return getDisplay().getHeight();
    }


    //获取屏幕顶部状态栏高度
    public static int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = App.getContext().getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Logger.d("" + statusBarHeight);
        return statusBarHeight;
    }

    //获取底部虚拟键盘的高度,若无则返回0
    public static int getNavigationBarHeight() {
        Resources resources = App.getContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            Logger.d(resources.getDimensionPixelSize(resourceId) + "");
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getToolbarHeight(){
//        TypedArray typedArray = App.getContext().getTheme().obtainStyledAttributes(ATTRS)
        return dip2px(32);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = App.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(int dpValue){
        final float scale = App.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}



