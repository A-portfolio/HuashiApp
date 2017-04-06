package net.muxi.huashiapp.util;

import android.text.TextUtils;

import net.muxi.huashiapp.App;

/**
 * Created by ybao on 17/2/9.
 */

public class UserUtil {

    /**
     * 生成学生对应的几个学年
     * @param length
     * @return
     */
    public static String[] generateYears(int length){
        String[] years = new String[length];
        int startYear;
        if (!TextUtils.isEmpty(App.sUser.sid) && App.sUser.sid.length() > 4){
            String yearStr = App.sUser.sid.substring(0,4);
            startYear = Integer.parseInt(yearStr);
            for (int i = 0;i < length;i ++){
                years[i] = String.valueOf(startYear);
                startYear ++;
            }
            return years;
        }
        return null;
    }

    public static String[] generateHyphenYears(int length){
        String[] years = generateYears(length + 1);
        String[] yearsWithHyphen = new String[length];
        for (int i = 0;i < length;i ++){
            yearsWithHyphen[i] = years[i] + "-" + years[i+1];
        }
        return yearsWithHyphen;
    }

    public static String getStudentGrade(){
        if (!TextUtils.isEmpty(App.sUser.sid) && App.sUser.sid.length() > 4){
            String year = App.sUser.sid.substring(0,4);
            return year;
        }
        return "";
    }
}
