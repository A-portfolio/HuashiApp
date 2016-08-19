package net.muxi.huashiapp;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.alipay.euler.andfix.patch.PatchManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.bugly.crashreport.CrashReport;
import com.zhuge.analysis.stat.ZhugeSDK;

import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.PreferenceUtil;


/**
 * Created by ybao on 16/4/18.
 */
public class App extends Application {


    public static Context sContext;

    public static AppCompatActivity sActivity = null;

    //获取上次的已经登录的用户账号信息
    public static User sUser = new User();
    public static User sLibrarayUser = new User();

    private PreferenceUtil sp;
    private PatchManager mPatchManager;

    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
//        LeakCanary.install(this);
        sContext = getApplicationContext();

        CrashReport.initCrashReport(getApplicationContext(), "900043675", true);
        Fresco.initialize(this);
        sp = new PreferenceUtil();
// TODO: 16/8/6 andfix turn on
        try {
            mPatchManager = new PatchManager(this);
            mPatchManager.init(BuildConfig.VERSION_NAME);
            mPatchManager.loadPatch();
            Logger.d("andfix load patch");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!sp.getString(PreferenceUtil.LAST_APP_VERSION,"1.0").equals(BuildConfig.VERSION_NAME)){
            mPatchManager.removeAllPatch();
            sp.saveString(PreferenceUtil.LAST_APP_VERSION,BuildConfig.VERSION_NAME);
        }
        try {
            mPatchManager.addPatch(AppConstants.CACHE_DIR + "/" + AppConstants.APATCH_NAME);
            Logger.d(AppConstants.CACHE_DIR + "/" + AppConstants.APATCH_NAME);
        } catch (Exception e) {
            Logger.d("andfix not load");
            e.printStackTrace();
        }
        

//        ZhugeSDK.getInstance().openDebug();
//        //必须在init之前调用
//        //禁止收集用户手机号码默认为收集
        ZhugeSDK.getInstance().disablePhoneNumber();
//        //禁止收集用户个人账户信息默认为收集
        ZhugeSDK.getInstance().disableAccounts();
        ZhugeSDK.getInstance().openLog();



        sUser.setSid(sp.getString(PreferenceUtil.STUDENT_ID, "0"));
        sUser.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD, ""));
        sLibrarayUser.setSid(sp.getString(PreferenceUtil.LIBRARY_ID, "0"));
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

    public static void clearLibUser(){
        PreferenceUtil sp = new PreferenceUtil();
        sp.clearString(PreferenceUtil.LIBRARY_ID);
        sp.clearString(PreferenceUtil.LIBRARY_PWD);
        sLibrarayUser.setSid("0");
        sLibrarayUser.setPassword("");
    }

    public static void saveUser(User user) {
        PreferenceUtil sp = new PreferenceUtil();
        sp.saveString(PreferenceUtil.STUDENT_ID, user.getSid());
        sp.saveString(PreferenceUtil.STUDENT_PWD, user.getPassword());
        sUser = user;
        Logger.d("id:" + sUser.getSid() + "\tpwd:" + sUser.getPassword());
    }

    public static void clearUser(){
        PreferenceUtil sp = new PreferenceUtil();
        sp.clearString(PreferenceUtil.STUDENT_ID);
        sp.clearString(PreferenceUtil.STUDENT_PWD);
        sUser.setSid("0");
        sUser.setPassword("");
    }

}
