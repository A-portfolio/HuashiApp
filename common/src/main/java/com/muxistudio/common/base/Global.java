package com.muxistudio.common.base;

import android.app.Application;

/**
 * Created by fengminchao on 18/3/19
 * 存放全局对象，方便共享
 */

public class Global {

    public static Application sApp;

    /**
     * 获取上层 application 对象
     * @return
     */
    public static Application getApplication(){
        return sApp;
    }

    /**
     * 上层 application 在 oncreate 时调用此方法
     * @param app
     */
    public static void setApplication(Application app){
        sApp = app;
    }
}
