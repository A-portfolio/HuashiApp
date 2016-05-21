package net.muxi.huashiapp.common.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.muxi.huashiapp.common.service.AlarmReceiveer;

import java.util.Calendar;

/**
 * Created by ybao on 16/5/16.
 */
public class AlarmUtil {

    public static void register(Context context){
        Calendar today = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        today.set(Calendar.HOUR_OF_DAY,21);
        today.set(Calendar.MINUTE,12);
        today.set(Calendar.SECOND,3);

        if (now.after(today)){
            return;
        }

        Intent intent = new Intent("net.muxi.huashiapp.alarm");
        intent.setClass(context, AlarmReceiveer.class);

        Log.d("time","set the time");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,200,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,today.getTimeInMillis(),pendingIntent);
    }
}
