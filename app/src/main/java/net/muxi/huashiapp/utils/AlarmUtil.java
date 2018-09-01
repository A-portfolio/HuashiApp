package net.muxi.huashiapp.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.muxistudio.appcommon.Constants;
import com.muxistudio.common.util.Logger;

import java.util.Locale;
import net.muxi.huashiapp.service.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by ybao on 16/5/16.
 */
public class AlarmUtil {

    public static void register(Context context) {

        //提醒时间点
        final int[] REMIND_TIME = {9, 15, 20};
        //提醒时间点个数
        final int ALARM_NUM = REMIND_TIME.length;

        Calendar[] calendars = new Calendar[ALARM_NUM];
        Calendar now = Calendar.getInstance();

        for (int i = 0; i < ALARM_NUM; i++) {
            calendars[i] = Calendar.getInstance();

            setTimeAround(calendars[i],REMIND_TIME[i]);
            Logger.d(calendars[i].get(Calendar.HOUR_OF_DAY) + " " + calendars[i].get(Calendar.MINUTE));
            if (now.after(calendars[i])){
                continue;
            }
            SimpleDateFormat format = new SimpleDateFormat("MM-dd-HH-mm-ss", Locale.CHINESE);
            Logger.d(format.format(calendars[i].getTime()));

            Intent intent = new Intent("net.muxi.huashiapp.alarm");
            intent.setClass(context, AlarmReceiver.class);
            intent.putExtra(Constants.ALARMTIME, i);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i , intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendars[i].getTimeInMillis(), pendingIntent);
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
        int minute = random.nextInt(60);
        if (minute >= 30) {
            hour = hour - 1;
        }
        int second = random.nextInt(60);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        Logger.d(calendar.get(Calendar.HOUR_OF_DAY) + "");
    }
}
