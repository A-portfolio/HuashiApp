package net.muxi.huashiapp.common.util;

import java.util.Calendar;

/**
 * Created by ybao on 16/8/27.
 */
public class NoDoubleClickUtil {

    private static long lastClickTime = 0;
    //最小点击事件间隔
    private static final int MIN_CLICK_TIME_DELAY = 1000;

    public static boolean isDoubleClick(){
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_TIME_DELAY){
            lastClickTime = currentTime;
            return false;
        }else {
            lastClickTime = currentTime;
            return true;
        }
    }
}
