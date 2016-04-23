package net.muxi.huashiapp.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ybao on 16/4/19.
 * 时间处理类
 */
public class DateUtil {

    public static String toDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("MM-dd");
        return dateFormat.format(date);
    }


    public static String toWeek(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("E");
        return dateFormat.format(date);
    }

    public static String toWeek(Date date,int distance){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,distance);
        return toWeek(calendar.getTime());
    }

    //获取指定的日期
    public static String getTheDate(Date date, int distance) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, distance);
        return toDate(calendar.getTime());
    }

    //获取本周的所有日期,用在课程表里面
    public static List<String> getTheWeekDate() {
        List<String> dateInWeek = new ArrayList<String>();

        Calendar calendar = Calendar.getInstance();
        Date curDate = calendar.getTime();
        String theWeek = toWeek(curDate);
        int i = 0;
        while(! theWeek.equals("星期一") ){
            i --;
            theWeek = toWeek(curDate,i);
        }
        calendar.setTime(curDate);
        for (int j = 0 ;j < 7 ;j ++){
            dateInWeek.add(j,getTheDate(curDate,i));
            i ++;
        }
        return dateInWeek;
    }

}
