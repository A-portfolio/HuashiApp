package net.muxi.huashiapp;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.bugly.crashreport.CrashReport;
import com.zhuge.analysis.stat.ZhugeSDK;

import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.PreferenceUtil;


/**
 * Created by ybao on 16/4/18.
 */
public class App extends Application {

    public static Context sContext;

    //获取上次的已经登录的用户账号信息
    public static User sUser = new User();
    public static User sLibrarayUser = new User();

    private PreferenceUtil sp;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        Fresco.initialize(this);

        CrashReport.initCrashReport(getApplicationContext(), "900043675", BuildConfig.DEBUG);
        Fresco.initialize(this);
        sp = new PreferenceUtil();

//        //必须在init之前调用
//        //禁止收集用户手机号码默认为收集
        ZhugeSDK.getInstance().disablePhoneNumber();
//        //禁止收集用户个人账户信息默认为收集
        ZhugeSDK.getInstance().disableAccounts();
        if (BuildConfig.DEBUG) {
            ZhugeSDK.getInstance().openLog();
        }


        sUser.setSid(sp.getString(PreferenceUtil.STUDENT_ID, ""));
        sUser.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD, ""));
        sLibrarayUser.setSid(sp.getString(PreferenceUtil.LIBRARY_ID, ""));
        sLibrarayUser.setPassword(sp.getString(PreferenceUtil.LIBRARY_PWD, ""));
    }


    public static Context getContext() {
        return sContext;
    }

    public static void saveLibUser(User libUser) {
        PreferenceUtil sp = new PreferenceUtil();
        sp.saveString(PreferenceUtil.LIBRARY_ID, libUser.getSid());
        sp.saveString(PreferenceUtil.LIBRARY_PWD, libUser.getPassword());
        sLibrarayUser = libUser;
        Logger.d("id:" + sLibrarayUser.getSid() + "\tpwd:" + sLibrarayUser.getPassword());
    }

    public static void logoutLibUser(){
        PreferenceUtil sp = new PreferenceUtil();
        sp.clearString(PreferenceUtil.LIBRARY_ID);
        sp.clearString(PreferenceUtil.LIBRARY_PWD);
        PreferenceUtil.clearString(PreferenceUtil.ATTENTION_BOOK_IDS);
        PreferenceUtil.clearString(PreferenceUtil.BORROW_BOOK_IDS);
        sLibrarayUser.setSid("");
        sLibrarayUser.setPassword("");
    }

    public static void saveUser(User user) {
        PreferenceUtil sp = new PreferenceUtil();
        sp.saveString(PreferenceUtil.STUDENT_ID, user.getSid());
        sp.saveString(PreferenceUtil.STUDENT_PWD, user.getPassword());
        sUser = user;
        Logger.d("id:" + sUser.getSid() + "\tpwd:" + sUser.getPassword());
    }

    public static void logoutUser(){
        PreferenceUtil sp = new PreferenceUtil();
        sp.clearString(PreferenceUtil.STUDENT_ID);
        sp.clearString(PreferenceUtil.STUDENT_PWD);
        sUser.setSid("");
        sUser.setPassword("");
    }

    public static boolean isInfoLogin(){
        return !TextUtils.isEmpty(sUser.sid);
    }

    public static boolean isLibLogin(){
        return !TextUtils.isEmpty(sLibrarayUser.sid);
    }
}
