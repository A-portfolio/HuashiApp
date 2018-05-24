package net.muxi.huashiapp;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.backends.pipeline.BuildConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.common.base.Global;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;

import net.muxi.huashiapp.utils.MiPushUtil;

import static com.muxistudio.appcommon.Constants.UMENG_APP_KEY;

/**
 * Created by ybao on 16/4/18.
 */
public class App extends Application {

    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
        Global.setApplication(this);
        UserAccountManager.getInstance().initUser();
        Fresco.initialize(this);
        initBugly();
        initUMeng();
        initARouter(this);
        MiPushUtil.initMiPush(this);
    }

    private void initBugly() {
        if (!BuildConfig.DEBUG) {
            CrashReport.initCrashReport(getApplicationContext(), "900043675", BuildConfig.DEBUG);
        }
    }

    private void initUMeng() {
        UMConfigure.init(this, UMENG_APP_KEY , null, UMConfigure.DEVICE_TYPE_PHONE, null);
    }

    private void initARouter(Application app) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            //开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
            ARouter.printStackTrace();
        }

        ARouter.init(app);

    }

    public static Context getContext() {
        return sContext;
    }

//    public static void saveLibUser(User libUser) {
//        PreferenceUtil.saveString(PreferenceUtil.LIBRARY_ID, libUser.getSid());
//        PreferenceUtil.saveString(PreferenceUtil.LIBRARY_PWD, libUser.getPassword());
//        sLibrarayUser = libUser;
//    }
//
//    public static void logoutLibUser() {
//        PreferenceUtil.clearString(PreferenceUtil.LIBRARY_ID);
//        PreferenceUtil.clearString(PreferenceUtil.LIBRARY_PWD);
//        PreferenceUtil.clearString(PreferenceUtil.ATTENTION_BOOK_IDS);
//        PreferenceUtil.clearString(PreferenceUtil.BORROW_BOOK_IDS);
//        PreferenceUtil.clearString(PreferenceUtil.PHPSESSID);
//        sLibrarayUser.setSid("");
//        sLibrarayUser.setPassword("");
//        CcnuCrawler2.clearCookieStore();
//    }

//    @De
//    public static void saveUser(User user) {
//        PreferenceUtil.saveString(PreferenceUtil.STUDENT_ID, user.getSid());
//        PreferenceUtil.saveString(PreferenceUtil.STUDENT_PWD, user.getPassword());
//        sUser = user;
//    }

//    public static void logoutUser() {
//        PreferenceUtil.clearString(PreferenceUtil.STUDENT_ID);
//        PreferenceUtil.clearString(PreferenceUtil.STUDENT_PWD);
//        PreferenceUtil.clearString(PreferenceUtil.BIG_SERVER_POOL);
//        PreferenceUtil.clearString(PreferenceUtil.JSESSIONID);
//        PreferenceUtil.clearString(PreferenceUtil.PHPSESSID);
//        sUser.setSid("");
//        sUser.setPassword("");
//        HuaShiDao dao = new HuaShiDao();
//        dao.deleteAllCourse();
//        CcnuCrawler2.clearCookieStore();
//    }

//    public static boolean isInfoLogin() {
//        return !TextUtils.isEmpty(sUser.sid);
//    }
//
//    // 判断是否已经登陆
//    public static boolean isLibLogin() {
//        String phpSess = PreferenceUtil.getString(PreferenceUtil.PHPSESSID);
//        if(!phpSess.equals("")||!TextUtils.isEmpty(phpSess)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
}
