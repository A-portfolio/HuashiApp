package net.muxi.huashiapp;

import android.app.Application;
import android.content.Context;

import com.zhuge.analysis.stat.ZhugeSDK;


/**
 * Created by ybao on 16/4/18.
 */
public class App extends Application {

    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        ZhugeSDK.getInstance().openDebug();
        //必须在init之前调用
//禁止收集用户手机号码默认为收集
        ZhugeSDK.getInstance().disablePhoneNumber();
//禁止收集用户个人账户信息默认为收集
        ZhugeSDK.getInstance().disableAccounts();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
