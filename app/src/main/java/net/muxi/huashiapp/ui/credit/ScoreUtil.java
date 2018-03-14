package net.muxi.huashiapp.ui.credit;

import android.text.TextUtils;

/**
 * Created by ybao on 17/2/10.
 */

public class ScoreUtil {

    /**
     * 专业必修课  -> 专业课必修
     * @param s
     * @return
     */
    public static String toCategory(String s){
        if (s != null && s.length() > 4){
            return s.substring(0,2) + "课" + s.substring(2,4);
        }
        return null;
    }

    //分为两种情况: cate1 cate2 是课程的分类 如果课程的标签是 专业主干课程的话 cate1= 专业+课 cate2= 主干
    //还有一种情况是素质课 课程的标签是 素质选修 cate1 =素质+课 cate2 = 选修
    public static String toCate1(String s){
        if(TextUtils.isEmpty(s)){
            return null;
        }else{
            return s.substring(0,2) + "课";
        }
    }

    public static String toCate2(String s){
        if(TextUtils.isEmpty(s)){
            return null;
        }else{
            return s.substring(2,4) + "课";
        }
    }
}
