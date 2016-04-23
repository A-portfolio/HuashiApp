package net.muxi.huashiapp.common.util;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import net.muxi.huashiapp.App;

/**
 * Created by ybao on 16/4/20.
 */
public class DimensUtil {

    public static Context sContext = App.getContext();

    public static Display getDisplay(){
        WindowManager windowManager = (WindowManager) sContext.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay();
    }

    public static int getScreenWidth(){
        return getDisplay().getWidth();
    }

    public static int getScreenHeight(){
        return getDisplay().getHeight();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px( float dpValue) {
        final float scale = App.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip( float pxValue) {
        final float scale = App.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}

}
