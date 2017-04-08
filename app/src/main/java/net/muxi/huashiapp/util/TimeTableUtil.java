package net.muxi.huashiapp.util;

import android.text.TextUtils;

import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Course;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by ybao on 16/7/27.
 */
public class TimeTableUtil {

    /**
     * 上课周数是否含有本周
     *
     * @param week    当前周
     * @param weekStr 周数字符串
     */
    public static boolean isThisWeek(int week, String weekStr) {
        if (TextUtils.isEmpty(weekStr)) {
            return false;
        }
        String[] weeks = weekStr.split(",");
        for (String s : weeks) {
            if (s.equals(String.valueOf(week))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取某个位置的所有的课程
     *
     * @param course     当前位置的课程
     * @param allCourses 所有课程的列表
     */
    public static List<Course> getAllCoursesInPosition(Course course, List<Course> allCourses) {
        List<Course> curPosCourses = new ArrayList<>();
        for (Course oneCourse : allCourses) {
            if (oneCourse.getDay().equals(course.getDay())
                    && oneCourse.getStart() == course.getStart()
                    && oneCourse.getDuring() == course.getDuring()) {
                curPosCourses.add(oneCourse);
            }
        }
        return curPosCourses;
    }

    /**
     * 获取课程背景颜色
     *
     * @param colorNumber 颜色值
     */
    public static int getCourseBg(int colorNumber) {
        int color = 0;
        switch (colorNumber) {
            case 0:
                color = R.drawable.ripple_orange;
                break;
            case 1:
                color = R.drawable.ripple_blue;
                break;
            case 2:
                color = R.drawable.ripple_green;
                break;
            case 3:
                color = R.drawable.ripple_yellow;
                break;
        }
        return color;
    }

    public static int getCourseBgAccordDay(String day) {
        int[] colors = {R.drawable.ripple_green_light,
                R.drawable.ripple_yellow,
                R.drawable.ripple_blue,
                R.drawable.ripple_orange,
                R.drawable.ripple_green};
        for (int i = 0; i < Constants.WEEKDAYS_XQ.length; i++) {
            if (day.equals(Constants.WEEKDAYS_XQ[i])) {
                return colors[i % 5];
            }
        }
        return colors[0];
    }

    public static String simplifyCourse(String course) {
        if (course.length() > 8) {
            return course.substring(0, 7) + "...";
        } else {
            return course;
        }
    }

    /**
     * 返回课程时间
     *
     * @param time    课程的第几节
     * @param isStart 是否是上课开始的时间
     */
    public static String getCourseTime(int time, boolean isStart) {
        String s = "";
        if (isStart) {
            int start = time / 2 * 2 + 8;
            s += (start < 10 ? "0" + start : start);
            s += ":";
            s += time % 2 == 0 ? "55" : "00";
            return s;
        }

        int end = time - 1 + 8;
        s += end < 10 ? "0" + end : end;
        s += ":";
        s += time % 2 == 0 ? "40" : "45";
        return s;
    }

    /**
     * 获取当前周的周次
     */
    public static int getCurWeek() {
        PreferenceUtil sp = new PreferenceUtil();
        int day = DateUtil.getDayInWeek(new Date(System.currentTimeMillis()));
        String defalutDate = DateUtil.getTheDateInYear(new Date(System.currentTimeMillis()),
                1 - day);
        int curWeek;
        curWeek = (int) DateUtil.getDistanceWeek(
                sp.getString(PreferenceUtil.FIRST_WEEK_DATE, defalutDate),
                DateUtil.toDateInYear(new Date(System.currentTimeMillis()))) + 1;
        curWeek = curWeek <= Constants.WEEKS_LENGTH ? curWeek : Constants.WEEKS_LENGTH;
        curWeek = curWeek >= 1 ? curWeek : 1;
        return curWeek;
    }

    public static void saveCurWeek(int week) {
        Date date = new Date(System.currentTimeMillis());
        int day = DateUtil.getDayInWeek(date);
        int distance = 1 - day - (week - 1) * 7;
        PreferenceUtil.saveString(PreferenceUtil.FIRST_WEEK_DATE,
                DateUtil.getTheDateInYear(date, distance));
    }

    /**
     * 获取选择的周期的周次
     *
     * @param date 一周的第一天日期
     */
    public static int getSelectedWeek(Date date) {
        String defalutDate = DateUtil.getTheDateInYear(date,
                1 - DateUtil.getDayInWeek(new Date(System.currentTimeMillis())));
        int selectWeek = (int) DateUtil.getDistanceWeek(
                PreferenceUtil.getString(PreferenceUtil.FIRST_WEEK_DATE, defalutDate),
                DateUtil.toDateInYear(new Date(System.currentTimeMillis()))) + 1;
        selectWeek = selectWeek <= Constants.WEEKS_LENGTH ? selectWeek : Constants.WEEKS_LENGTH;
        selectWeek = selectWeek >= 1 ? selectWeek : 1;
        return selectWeek;
    }

    public static List<Course> getCurWeekCourse(List<Course> allCourseList) {
        List<Course> courseList = new ArrayList<>();
        for (int i = 0; i < allCourseList.size(); i++) {
            String weeks = allCourseList.get(i).getWeeks();
            if (isThisWeek(getCurWeek(), weeks)) {
                courseList.add(allCourseList.get(i));
            }
        }
        return courseList;
    }

    public static List<Course> getTodayCourse(List<Course> allCourseList) {
        List<Course> courseList = new ArrayList<>();
        for (int i = 0; i < allCourseList.size(); i++) {
            String weeks = allCourseList.get(i).getWeeks();
            String day = allCourseList.get(i).getDay();
            Logger.d(day);
            if (isThisWeek(getCurWeek(), weeks) && day.equals(
                    Constants.WEEKDAYS_XQ[
                            DateUtil.getDayInWeek(new Date(System.currentTimeMillis()))
                                    - 1])) {
                courseList.add(allCourseList.get(i));
            }
        }
        Collections.sort(courseList,(c1,c2) -> c1.start - c2.start);
        return courseList;
    }

    /**
     * 将 weekday 转换为 int 类型 星期一 -> 0
     */
    public static int weekday2num(String weekday) {
        Logger.d(weekday);
        int i = 0;
        while (!weekday.equals(Constants.WEEKDAYS_XQ[i])) {
            i++;
            if (i >= 7) {
                break;
            }
        }
        return i;
    }

    public static boolean isSingleWeeks(List<Integer> weekList) {
        boolean b = true;
        if (weekList.size() < 2) {
            return false;
        }
        if (weekList.get(0) % 2 == 1) {
            for (int i = 0; i < weekList.size() - 1; i++) {
                if (weekList.get(i + 1) - weekList.get(i) != 2) {
                    b = false;
                    break;
                }
            }
            return b;
        } else {
            return false;
        }
    }

    public static boolean isDoubleWeeks(List<Integer> weekList) {
        boolean b = true;
        if (weekList.size() < 2) {
            return false;
        }
        if (weekList.get(0) % 2 == 0) {
            for (int i = 0; i < weekList.size() - 1; i++) {
                if (weekList.get(i + 1) - weekList.get(i) != 2) {
                    b = false;
                    break;
                }
            }
            return b;
        } else {
            return false;
        }
    }

    public static boolean isContinuOusWeeks(List<Integer> weekList) {
        if (weekList.size() < 2) {
            return false;
        }
        if (weekList.get(weekList.size() - 1) - weekList.get(0) == weekList.size() - 1) {
            return true;
        } else {
            return false;
        }
    }

    public static String getDisplayWeeks(List<Integer> weekList) {
        String s;
        int start;
        int end;
        if (isSingleWeeks(weekList)) {
            start = weekList.get(0);
            end = weekList.get(weekList.size() - 1) + 1;
            s = String.format("%d-%d周单", start, end);
        } else if (isDoubleWeeks(weekList)) {
            start = weekList.get(0) - 1;
            end = weekList.get(weekList.size() - 1);
            s = String.format("%d-%d周双", start, end);
        } else if (isContinuOusWeeks(weekList)) {
            start = weekList.get(0);
            end = weekList.get(weekList.size() - 1);
            s = String.format("%d-%d周", start, end);
        } else {
            s = TextUtils.join(",", weekList);
            s += "周";
        }
        return s;
    }
}
