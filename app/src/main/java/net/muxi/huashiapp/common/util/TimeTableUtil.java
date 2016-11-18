package net.muxi.huashiapp.common.util;

import android.text.TextUtils;
import net.muxi.huashiapp.AppConstants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Course;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ybao on 16/7/27.
 */
public class TimeTableUtil {

    /**
     * 上课周数是否含有本周
     *
     * @param week    当前周
     * @param weekStr 周数字符串
     * @return
     */
    public static boolean isThisWeek(int week, String weekStr) {
        if (TextUtils.isEmpty(weekStr)){
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
     * @return
     */
    public static List<Course> getAllCoursesInPosition(Course course, List<Course> allCourses) {
        List<Course> curPosCourses = new ArrayList<>();
        for (Course oneCourse : allCourses) {
            if (oneCourse.getDay().equals(course.getDay()) && oneCourse.getStart() == course.getStart() && oneCourse.getDuring() == course.getDuring()) {
                curPosCourses.add(oneCourse);
            }
        }
        return curPosCourses;
    }

    /**
     * 获取课程背景颜色
     *
     * @param colorNumber 颜色值
     * @param type        是否有一起的课程 0为无,1为有
     * @return
     */
    public static int getCourseBg(int colorNumber, int type) {
        int color = 0;
        if (type == 0) {
            switch (colorNumber) {
                case 0:
                    color = R.drawable.bg_simple_class_green;
                    break;
                case 1:
                    color = R.drawable.bg_simple_class_orange;
                    break;
                case 2:
                    color = R.drawable.bg_simple_class_pink;
                    break;
                case 3:
                    color = R.drawable.bg_simple_class_purple;
                    break;
            }
        } else {
            switch (colorNumber) {
                case 0:
                    color = R.drawable.bg_class_green;
                    break;
                case 1:
                    color = R.drawable.bg_class_orange;
                    break;
                case 2:
                    color = R.drawable.bg_class_pink;
                    break;
                case 3:
                    color = R.drawable.bg_class_purple;
                    break;
            }
        }
        return color;
    }

    public static String simplifyCourse(String course) {
        if (course.length() > 12) {
            return course.substring(0, 11) + "...";
        } else {
            return course;
        }
    }

    /**
     * 返回课程时间
     *
     * @param time    课程的第几节
     * @param isStart 是否是上课开始的时间
     * @return
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
     *
     * @return
     */
    public static int getCurWeek() {
        PreferenceUtil sp = new PreferenceUtil();
        int day = DateUtil.getDayInWeek(new Date(System.currentTimeMillis()));
        String defalutDate = DateUtil.getTheDateInYear(new Date(System.currentTimeMillis()), 1 - day);
        int curWeek;
        curWeek = (int) DateUtil.getDistanceWeek(sp.getString(PreferenceUtil.FIRST_WEEK_DATE, defalutDate), DateUtil.toDateInYear(new Date(System.currentTimeMillis()))) + 1;
        curWeek = curWeek <= AppConstants.WEEKS_LENGTH ? curWeek : AppConstants.WEEKS_LENGTH;
        curWeek = curWeek >= 1 ? curWeek : 1;
        return curWeek;
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
            if (isThisWeek(getCurWeek(), weeks) && day.equals(AppConstants.WEEKDAYS[DateUtil.getDayInWeek(new Date(System.currentTimeMillis())) - 1])) {
                courseList.add(allCourseList.get(i));
            }
        }
        return courseList;
    }
}
