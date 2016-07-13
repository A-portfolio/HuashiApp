package net.muxi.huashiapp.common.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import net.muxi.huashiapp.AppConstants;
import net.muxi.huashiapp.common.service.AlarmReceiveer;

import java.util.Calendar;

/**
 * Created by ybao on 16/5/16.
 */
public class AlarmUtil {

    public static void register(Context context) {
        //一天有三个时间点提醒
        Calendar firstTime = Calendar.getInstance();
        Calendar secondTime = Calendar.getInstance();
        Calendar thirdTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        //三次推送时间
        firstTime.set(Calendar.HOUR_OF_DAY, 9);
        secondTime.set(Calendar.HOUR_OF_DAY, 15);
        thirdTime.set(Calendar.HOUR_OF_DAY, 20);
        Intent intent = new Intent("net.muxi.huashiapp.alarm");
        intent.setClass(context, AlarmReceiveer.class);

        if (now.after(thirdTime)) {
            return;
        } else if (now.after(secondTime)) {
            intent.putExtra(AppConstants.ALARMTIME, "thirdTime");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 200, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, secondTime.getTimeInMillis(), pendingIntent);
        } else if (now.after(firstTime)) {
            intent.putExtra(AppConstants.ALARMTIME, "secondTime");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 200, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, secondTime.getTimeInMillis(), pendingIntent);
        } else {
            intent.putExtra(AppConstants.ALARMTIME, "firstTime");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 200, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, secondTime.getTimeInMillis(), pendingIntent);
        }

    }
}
