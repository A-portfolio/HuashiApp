package com.muxistudio.appcommon.utils;

import android.text.TextUtils;

import com.muxistudio.appcommon.user.UserAccountManager;

public class UserUtil {

    /**
     * 生成学生对应的几个学年
     * @param length
     * @return
     */
    public static String[] generateYears(int length){
        String[] years = new String[length];
        int startYear;
        if (!TextUtils.isEmpty(UserAccountManager.getInstance().getInfoUser().sid) && UserAccountManager.getInstance().getInfoUser().sid.length() > 4){
            String yearStr = UserAccountManager.getInstance().getInfoUser().sid.substring(0,4);
            startYear = Integer.parseInt(yearStr);
            for (int i = 0;i < length;i ++){
                years[i] = String.valueOf(startYear);
                startYear ++;
            }
            return years;
        }
        return null;
    }


    /**
     * 生成一个类似 2016-2017 这样的两个连接一起来的字符串
     * @param length
     * @return
     */
    public static String[] generateHyphenYears(int length){
        String[] years = generateYears(length + 1);
        String[] yearsWithHyphen = new String[length];
        for (int i = 0;i < length;i ++){
            yearsWithHyphen[i] = years[i] + "-" + years[i+1];
        }
        return yearsWithHyphen;
    }

    public static String getStudentGrade(){
        if (!TextUtils.isEmpty(UserAccountManager.getInstance().getInfoUser().sid) && UserAccountManager.getInstance().getInfoUser().sid.length() > 4){
            String year = UserAccountManager.getInstance().getInfoUser().sid.substring(0,4);
            return year;
        }
        return "";
    }

    /**
     * 获取学生入学年份
     * @return 入学年份
     */
    public static String getStudentFirstYear(){
        //todo ensure to be called only when the user has already login!
        return UserAccountManager.getInstance().getInfoUser().sid.substring(0,4);
    }
}
