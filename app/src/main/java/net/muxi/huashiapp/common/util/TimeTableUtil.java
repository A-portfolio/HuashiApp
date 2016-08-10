package net.muxi.huashiapp.common.util;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ybao on 16/7/27.
 */
public class TimeTableUtil {

    /**
     * 上课周数是否含有本周
     * @param week 当前周
     * @param weekStr 周数字符串
     * @return
     */
    public static boolean isThisWeek(int week,String weekStr){
        String[] weeks = weekStr.split(",");
        for (String s : weeks){
            if (s.equals(String.valueOf(week))){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取某个位置的所有的课程
     * @param course 当前位置的课程
     * @param allCourses 所有课程的列表
     * @return
     */
    public static List<Course> getAllCoursesInPosition(Course course, List<Course> allCourses ){
        List<Course> curPosCourses = new ArrayList<>();
        for (Course oneCourse : allCourses){
            if (oneCourse.getDay().equals(course.getDay()) && oneCourse.getStart() == course.getStart() && oneCourse.getDuring() == course.getDuring()){
                curPosCourses.add(oneCourse);
            }
        }
        return curPosCourses;
    }

    /**
     * 获取课程背景颜色
     * @param colorNumber 颜色值
     * @param type 是否有一起的课程 0为无,1为有
     * @return
     */
    public static int getCourseBg(int colorNumber,int type) {
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
        }else {
            switch (colorNumber){
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
}
