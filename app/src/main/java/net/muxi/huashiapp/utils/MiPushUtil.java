package net.muxi.huashiapp.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.muxistudio.common.util.Logger;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

public class MiPushUtil {
    
    private static void openLogcat(Context context) {
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String s) {

            }

            @Override
            public void log(String s) {
                Logger.d(s);
            }

            @Override
            public void log(String s, Throwable throwable) {
                Logger.d(s);
            }
        };

        //注：默认情况下，我们会将日志内容写入SDCard/Android/data/app pkgname/files/MiPushLog目录下的文件
        // 如果app需要关闭写日志文件功能（不建议关闭），只需要调用Logger.disablePushFileLog(context)即可。
        com.xiaomi.mipush.sdk.Logger.setLogger(context,newLogger);
    }

    private static boolean shouldInit(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static void initMiPush(Context context){
        if(shouldInit(context)){
            MiPushClient.registerPush(context, com.muxistudio.appcommon.Constants.MI_PUSH_APPID,
                    com.muxistudio.appcommon.Constants.MI_PUSH_APPKEY);
        }
            openLogcat(context);
    }
}
