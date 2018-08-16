package net.muxi.huashiapp.util;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 将选择dialog中的选择的结果数据进行转换
 */
public class ScoreCreditUtils {

    /**
     * 在返回的list中没有加上term的分隔符
     * @param terms
     * @return 返回课程选择对应的名称list
     */
    public static List<String> parseTerm2Names(boolean terms[]){
        List<String> termList = new ArrayList<>();
        if(terms[0]&&terms[1]&&terms[2]){
            termList.add("第一学期/第二学期/第三学期");
            return termList;
        }
        for(int i=0;i<terms.length;i++) {
            if (terms[i]) {
                switch (i) {
                    case 0:
                        termList.add("第一学期");
                        break;
                    case 1:
                        termList.add("第二学期");
                        break;
                    case 2:
                        termList.add("第三学期");
                        break;
                }
            }
        }
        return termList;
    }

    /**
     *
     * @param terms
     * @return 返回term选择所对应的code list
     */
    public static List<String> parseTerm2Code(boolean terms[]){
        List<String> codeList = new ArrayList<>();
        if(terms[0]&&terms[1]&&terms[2]){
            codeList.add("");
            return codeList;
        }
        for(int i=0;i<terms.length;i++) {
            if (terms[i]) {
                switch (i) {
                    case 0:
                        codeList.add("3");
                        break;
                    case 1:
                        codeList.add("12");
                        break;
                    case 2:
                        codeList.add("16");
                        break;
                }
            }
        }
        return codeList;
    }

    /**
     *
     * @param termNames 学期名称数组
     * @param separator 学期名称分隔符 只能是单个字符
     * @return 生成的字符串可能格式为 第一学期/第二学期
     */
    public static String parseNames2String(List<String> termNames,char separator){
        if(termNames.size() == 1)
            return termNames.get(0);
        String names = "";
        for(String name:termNames){
            names = names + name + separator;
        }
        names = names.substring(0,names.length());
        return names;
    }

    @SuppressLint("DefaultLocale")
    public static String parseYears2Title(List<String> years){
        if(years.size() == 1){
            int start = Integer.parseInt(years.get(0));
            int end   =  start + 1;
            return String.format("第%d-%d学年",start,end);
        }
        String start = years.get(0), end = years.get(years.size() - 1);
        return String.format("第%s-%s学年",start,end);
    }


    public static List<String> getCourseType(HashMap<String,Boolean> selectedCourse){
        List<String> courses = new ArrayList<>();
        Set<String> keys = selectedCourse.keySet();
        for(String key : keys){
            if(selectedCourse.get(key)){
                courses.add(key);
            }
        }
        return courses;
    }
}
