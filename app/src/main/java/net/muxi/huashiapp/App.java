package net.muxi.huashiapp;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.BuildConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.tinker.loader.app.ApplicationLike;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike;
import com.zhuge.analysis.stat.ZhugeSDK;

import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.ui.main.FetchPatchHandler;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.PreferenceUtil;


/**
 * Created by ybao on 16/4/18.
 */
public class App extends Application {

    public static String PHPSESSID ;
    public static Context sContext;
    //获取上次的已经登录的用户账号信息
    public static User sUser = new User();
    public static User sLibrarayUser = new User();

    private PreferenceUtil sp;

    private ApplicationLike tinkerApplicationLike;

    @Override
    public void onCreate() {
        super.onCreate();

        tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();

        TinkerPatch.init(tinkerApplicationLike)
                .reflectPatchLibrary()
                .setPatchRollbackOnScreenOff(true)
                .setPatchRestartOnSrceenOff(true);

        new FetchPatchHandler().fetchPatchWithInterval(3);

       /// PreferenceUtil.clearString(PreferenceUtil.JSESSIONID);
        sContext = getApplicationContext();
        sp = new PreferenceUtil();

        Log.d("stringss", "onCreate: "+PreferenceUtil.getString(PreferenceUtil.BIG_SERVER_POOL)+" "
        + PreferenceUtil.getString(PreferenceUtil.JSESSIONID));
        sUser.setSid(sp.getString(PreferenceUtil.STUDENT_ID, ""));
        sUser.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD, ""));
        sLibrarayUser.setSid(sp.getString(PreferenceUtil.LIBRARY_ID, ""));
        sLibrarayUser.setPassword(sp.getString(PreferenceUtil.LIBRARY_PWD, ""));
        PHPSESSID = PreferenceUtil.getString(PreferenceUtil.PHPSESSION_ID);

        Fresco.initialize(this);

        initZhuge();
        initBugly();
    }

    private void initBugly() {
        if (!BuildConfig.DEBUG) {
            CrashReport.initCrashReport(getApplicationContext(), "900043675", BuildConfig.DEBUG);
        }
    }

    private void initZhuge() {
        //禁止收集用户手机号码默认为收集
        ZhugeSDK.getInstance().disablePhoneNumber();
        //禁止收集用户个人账户信息默认为收集
        ZhugeSDK.getInstance().disableAccounts();
        if (BuildConfig.DEBUG) {
            ZhugeSDK.getInstance().openLog();
        }
    }

    public static Context getContext() {
        return sContext;
    }

    public static void saveLibUser(User libUser) {
        PreferenceUtil.saveString(PreferenceUtil.LIBRARY_ID, libUser.getSid());
        PreferenceUtil.saveString(PreferenceUtil.LIBRARY_PWD, libUser.getPassword());
        sLibrarayUser = libUser;
    }

    public static void logoutLibUser() {
        PreferenceUtil.clearString(PreferenceUtil.LIBRARY_ID);
        PreferenceUtil.clearString(PreferenceUtil.LIBRARY_PWD);
        PreferenceUtil.clearString(PreferenceUtil.ATTENTION_BOOK_IDS);
        PreferenceUtil.clearString(PreferenceUtil.BORROW_BOOK_IDS);
        sLibrarayUser.setSid("");
        sLibrarayUser.setPassword("");
    }

    public static void saveUser(User user) {
        PreferenceUtil.saveString(PreferenceUtil.STUDENT_ID, user.getSid());
        PreferenceUtil.saveString(PreferenceUtil.STUDENT_PWD, user.getPassword());
        sUser = user;
        Logger.d("id:" + sUser.getSid() + "\tpwd:" + sUser.getPassword());
    }

    public static void logoutUser() {
        PreferenceUtil.clearString(PreferenceUtil.STUDENT_ID);
        PreferenceUtil.clearString(PreferenceUtil.STUDENT_PWD);
        PreferenceUtil.clearString(PreferenceUtil.BIG_SERVER_POOL);
        PreferenceUtil.clearString(PreferenceUtil.JSESSIONID);
        PreferenceUtil.clearString(PreferenceUtil.PHPSESSION_ID);
        sUser.setSid("");
        sUser.setPassword("");
        HuaShiDao dao = new HuaShiDao();
        dao.deleteAllCourse();
    }

    public static boolean isInfoLogin() {
        return !TextUtils.isEmpty(sUser.sid);
    }

    public static boolean isLibLogin() {
        if(PreferenceUtil.getString(PreferenceUtil.PHPSESSION_ID).equals(""))
            return true;
        else
            return false;
        //return !TextUtils.isEmpty();
    }
}
