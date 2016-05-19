package net.muxi.huashiapp;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zhuge.analysis.stat.ZhugeSDK;

import net.muxi.huashiapp.common.data.LibrarayUser;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.util.PreferenceUtil;


/**
 * Created by ybao on 16/4/18.
 */
public class App extends Application {


    public static Context sContext;

    public static AppCompatActivity sActivity = null;

    //获取上次的已经登录的用户账号信息
    public static User sUser = new User();
    public static LibrarayUser sLibrarayUser= new LibrarayUser();

    private PreferenceUtil sp;

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
        sp = new PreferenceUtil();
        //delete after
        Log.d("app","oncreate");
        sp.saveString(PreferenceUtil.STUDENT_ID,"0");
        sUser.setSid(sp.getString(PreferenceUtil.STUDENT_ID, "0"));
        sUser.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD, ""));

        //delete after
        sp.saveString(PreferenceUtil.LIBRARY_ID,"0");
        sLibrarayUser.setLibraryId(sp.getString(PreferenceUtil.LIBRARY_ID, "0"));
        sLibrarayUser.setLibraryPwd(sp.getString(PreferenceUtil.LIBRARY_PWD, ""));

        sContext = getApplicationContext();
//        sp = new PreferenceUtil();
//        sUser = new User();
//        sUser.setSid(sp.getString(PreferenceUtil.STUDENT_ID, null));
//        sUser.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD, null));
//
//        sLibrarayUser = new LibrarayUser();
//        sLibrarayUser.setLibraryId(sp.getString(PreferenceUtil.LIBRARY_ID, null));
//        sLibrarayUser.setLibraryPwd(sp.getString(PreferenceUtil.LIBRARY_PWD, null));
//
//        sLibrarayUser = new LibrarayUser();

    }

    public static Context getContext() {
        return sContext;
    }


    public static void setCurrentActivity(AppCompatActivity activity) {
        sActivity = activity;
    }

    public static AppCompatActivity getCurrentActivity() {
        return sActivity;
    }
}
