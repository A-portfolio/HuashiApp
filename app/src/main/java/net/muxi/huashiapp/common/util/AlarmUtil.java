package net.muxi.huashiapp.common.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.muxi.huashiapp.AppConstants;
import net.muxi.huashiapp.common.service.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by ybao on 16/5/16.
 */
public class AlarmUtil {

    public static void register(Context context) {

        //提醒时间点个数
        final int ALARM_NUM = 3;
        //提醒时间点
        final int[] REMIND_TIME = {9, 15, 20};

        Calendar[] calendars = new Calendar[ALARM_NUM];
        Calendar now = Calendar.getInstance();
        Log.d("alarm","begin register");

        for (int i = 0; i < ALARM_NUM; i++) {
            calendars[i] = Calendar.getInstance();
//            setTimeAround(calendars[i], REMIND_TIME[i]);
            calendars[i].set(Calendar.HOUR_OF_DAY,REMIND_TIME[i]);
            calendars[i].set(Calendar.MINUTE,0);
            calendars[i].set(Calendar.SECOND,1);
            if (now.after(calendars[i])){
                continue;
            }
            SimpleDateFormat format = new SimpleDateFormat("MM-dd-HH-mm-ss");
            Logger.d(format.format(calendars[i].getTime()));

            Intent intent = new Intent("net.muxi.huashiapp.alarm");
            intent.setClass(context, AlarmReceiver.class);
            intent.putExtra(AppConstants.ALARMTIME, i);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i , intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendars[i].getTimeInMillis(), pendingIntent);
            Log.d("alarm","set alarmManager");
        }

    }

    /**
     * 设置时间在整点前后半小时随机发通知
     *
     * @param calendar
     * @param hour
     */
    public static void setTimeAround(Calendar calendar, int hour) {
        Random random = new Random();
        int minute = random.nextInt() * 60;
        if (minute >= 30) {
            hour = hour - 1;
        }
        int second = random.nextInt() * 60;
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
    }
}
