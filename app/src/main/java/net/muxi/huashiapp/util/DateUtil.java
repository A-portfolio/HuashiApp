package net.muxi.huashiapp.util;


import java.text.DateFormat;
import java.text.ParseException;
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
        DateFormat dateFormat = new SimpleDateFormat("MM/dd");
        return dateFormat.format(date);
    }

    public static String toToday(Date date){
        DateFormat dateFormat = new SimpleDateFormat("MM月dd日");
        return dateFormat.format(date);
    }

    public static String toDateInYear(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
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

    //获取两个日期相隔的周,用于课程表
    public static long getDistanceWeek(String date1,String date2){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date d1 = format.parse(date1);
            Date d2 = format.parse(date2);
            long week = (d2.getTime() - d1.getTime())/(24 * 60 * 60 * 1000 * 7);
            return week;
        }catch (ParseException e){
            e.printStackTrace();
        }
        return 0;
    }

    //获取指定的日期
    public static String getTheDateInYear(Date date, int distance) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, distance);
        return toDateInYear(calendar.getTime());
    }

    //获取指定的日期
    public static String getTheDate(Date date, int distance) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, distance);
        return toDate(calendar.getTime());
    }


    //获取当前日期
    public static String getToday(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return toToday(calendar.getTime());
    }


    /**
     * 根据日期获取
     * @param date
     * @return
     */
    public static String getWeek(Date date){
        String [] weeks = {"周日","周一","周二","周三","周四","周五","周六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week_index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0){
            week_index = 0;
        }
        return weeks[week_index];
    }


//    public static List<String> getTheWeekDate(int weekDistance) {
//        List<String> dateInWeek = new ArrayList<String>();
//
////        Calendar calendar = Calendar.getInstance();
//        Date curDate = new Date();
//        String theWeek = toWeek(curDate);
//        int i = 0;
//        while(! theWeek.equals("周一") ){
//            i --;
//            theWeek = toWeek(curDate,i);
//        }
//        for (int j = 0 ;j < 7 ;j ++){
//            dateInWeek.add(getTheDate(curDate,i + j + weekDistance * 7));
//        }
//        return dateInWeek;
//    }

    //获取今天是一周的第几天
    public static int getDayInWeek(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 1){
            w = 7;
        }
        return w;
    }

    //获取本周的所有日期,用在课程表里面,这边的 weekDistance 指代 距离本周的周数,例如本周为0,上周为-1
    public static List<String> getTheWeekDate(int weekDistance){
        List<String> dateInWeek = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        calendar.setTime(date);
        //求今天是一周的第几天
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 1){
            w = 7;
        }
        for (int j = 0;j < 7;j ++){
            dateInWeek.add(getTheDate(date,j - w + 1 + weekDistance * 7));
        }
        return dateInWeek;
    }

    /**
     * 比较两个 格式为 "yyyy-MM-dd"的日期的大小 前一个比后一个大 返回 true
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isAfter(String date1,String date2){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date d1 = format.parse(date1);
            Date d2 = format.parse(date2);
            return d1.after(d2);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取两个时间的秒差数
     * @param date1
     * @param date2
     * @return
     */
    public static long getSecondSpace(Date date1,Date date2){
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        long diff = date2.getTime() - date1.getTime();
        return (diff % nd % nh % nm );
    }


}
