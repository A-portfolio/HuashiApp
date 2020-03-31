package net.muxi.huashiapp;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.backends.pipeline.BuildConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.muxistudio.appcommon.data.User;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.appcommon.utils.UtilsExtensionKt;
import com.muxistudio.common.base.Global;
import com.muxistudio.common.util.PreferenceUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;

import net.muxi.huashiapp.utils.MiPushUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.muxistudio.appcommon.Constants.UMENG_APP_KEY;
import static com.muxistudio.common.util.DimensUtil.dp2px;

/**
 * Created by ybao on 16/4/18.
 */
//fixme 使用静态的sContext变量会造成类存泄漏
public class App extends Application {

    public static Context sContext;
    public static long sLastLogin;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        Global.setApplication(this);
        UserAccountManager.getInstance().initUser();
        Fresco.initialize(getContext(), setFrescoConfig());
        Application application = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                initBugly();
                initUMeng();
                //暂时没用到aRouter
                //initARouter(application);
                initX5();

            }
        }).start();

        sLastLogin = PreferenceUtil.getLong(PreferenceUtil.LAST_LOGIN_MOMENT);


        //UtilsExtensionKt.cache(sContext,user.sid,user.password);
    }


    private void initX5() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {

            }
        };
        QbSdk.initX5Environment(getApplicationContext(), cb);

    }


    private void initBugly() {
        if (!BuildConfig.DEBUG) {
            CrashReport.initCrashReport(getApplicationContext(), "900043675", BuildConfig.DEBUG);
        }
    }

    private void initUMeng() {
        UMConfigure.init(this, UMENG_APP_KEY, null, UMConfigure.DEVICE_TYPE_PHONE, null);
    }

    private void initARouter(Application app) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            //开启调试模式(如果在InstantRun看模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
            ARouter.printStackTrace();
        }

        ARouter.init(app);

    }

    public static Context getContext() {
        return sContext;
    }

    private ImagePipelineConfig setFrescoConfig(){

        //可详细设置缓存的文件夹等
        return ImagePipelineConfig.newBuilder(sContext)
                .setDownsampleEnabled(true)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .build();

    }

}