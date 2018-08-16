package net.muxi.huashiapp.util;

import android.annotation.SuppressLint;

import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.data.Score;

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
     *
     * @param terms
     * @return 返回课程选择对应的名称list
     */
    public static List<String> parseTerm2Names(boolean terms[]) {
        List<String> termList = new ArrayList<>();
        if (terms[0] && terms[1] && terms[2]) {
            termList.add("第一学期/第二学期/第三学期");
            return termList;
        }
        for (int i = 0; i < terms.length; i++) {
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
     * @param terms
     * @return 返回term选择所对应的code list
     */
    public static List<String> parseTerm2Code(boolean terms[]) {
        List<String> codeList = new ArrayList<>();
        if (terms[0] && terms[1] && terms[2]) {
            codeList.add("");
            return codeList;
        }
        for (int i = 0; i < terms.length; i++) {
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
     * @param termNames 学期名称数组
     * @param separator 学期名称分隔符 只能是单个字符
     * @return 生成的字符串可能格式为 第一学期/第二学期
     */
    public static String parseTermNames2String(List<String> termNames, char separator) {
        if (termNames.size() == 1)
            return termNames.get(0);
        String names = "";
        for (String name : termNames) {
            names = names + name + separator;
        }
        names = names.substring(0, names.length());
        return names;
    }

    /**
     * 如果year 的 list只有一个的话就自动加一 ，否则从一头一尾选择学年
     *
     * @param years 学年list 可能size为1
     * @return 返回格式化的学年字符串
     */
    @SuppressLint("DefaultLocale")
    public static String parseYears2Title(List<String> years) {
        if (years.size() == 1) {
            int start = Integer.parseInt(years.get(0));
            int end = start + 1;
            return String.format("第%d-%d学年", start, end);
        }
        String start = years.get(0), end = years.get(years.size() - 1);
        return String.format("第%s-%s学年", start, end);
    }

    /**
     * 获取选择的课程名称
     *
     * @param selectedCourse 选择的课程名称作为key，并且value 是 true
     * @return
     */
    public static List<String> getSelectedCourseType(HashMap<String, Boolean> selectedCourse) {
        List<String> courses = new ArrayList<>();
        Set<String> keys = selectedCourse.keySet();
        for (String key : keys) {
            if (selectedCourse.get(key)) {
                courses.add(key);
            }
        }
        return courses;
    }

    /**
     * 将对应的课程类型的学分相加并且存储在map里面
     *
     * @param credits 成绩数组
     * @return {@link Constants } CLASS_TYPE对应的集中课程分类相加,但是多增加了一条所有课的学分的key
     */
    public static HashMap<String, Double> getSortedCourseCreditMap(List<Score> credits) {
        HashMap<String, Double> creditMap = new HashMap<>();
        //初始化hashmap
        for (String credit : Constants.CLASS_TYPE) {
            creditMap.put(credit, 0d);
        }

        double allCredit = 0;

        for (Score credit : credits) {

            allCredit += Double.parseDouble(credit.credit);

            for (int i = 0; i < Constants.CLASS_TYPE.length; i++) {
                String type = Constants.CLASS_TYPE[i];
                if (type.equals(credit.kcxzmc)) {
                    double addUp = creditMap.get(type) + Double.parseDouble(credit.credit);
                    creditMap.put(type, addUp);
                    break;
                }
                if (i == Constants.CLASS_TYPE.length - 1) {
                    double addUp = creditMap.get("其他") + Double.parseDouble(credit.credit);
                    creditMap.put("其他", addUp);
                }
            }
        }
        creditMap.put(Constants.ALL_CREDIT, allCredit);
        return creditMap;
    }

    /**
     * 将课程分类写进HashMap
     *
     * @param credits 成绩list
     * @return 返回根据类型好的credit
     */
    public static HashMap<String, List<Score>> getSortedCourseTypeMap(List<Score> credits) {
        List<Score> zyzg = new ArrayList<>();
        List<Score> gxfz = new ArrayList<>();
        List<Score> tshx = new ArrayList<>();
        List<Score> tsbx = new ArrayList<>();
        List<Score> tsxx = new ArrayList<>();
        List<Score> other = new ArrayList<>();

        HashMap<String,List<Score>> sortedMap = new HashMap<>();

        sortedMap.put(Constants.CLASS_TYPE[0],zyzg);
        sortedMap.put(Constants.CLASS_TYPE[1],gxfz);
        sortedMap.put(Constants.CLASS_TYPE[2],tshx);
        sortedMap.put(Constants.CLASS_TYPE[3],tsbx);
        sortedMap.put(Constants.CLASS_TYPE[4],tsxx);
        sortedMap.put(Constants.CLASS_TYPE[5],other);

        for (Score credit : credits) {

            for (int i = 0; i < Constants.CREDIT_CATEGORY.length; i++) {
                String type = Constants.CREDIT_CATEGORY[i];

                if (type.equals(credit.kcxzmc)) {
                    List<Score> c = sortedMap.get(type);
                    c.add(credit);
                    sortedMap.put(type,c);
                    break;
                }

                if(i== Constants.CLASS_TYPE.length - 1){
                    List<Score> c = sortedMap.get("其他");
                    c.add(credit);
                    sortedMap.put("其他",c);
                }

            }
        }
        return sortedMap;
    }

    /**
     * 通过课程名称和学分的map 将数据填入一个list
     * @return 填充成功的list
     */
    public static List<Double> getCreditValues(HashMap<String,Double> creditMap){
        List<Double> creditValues = new ArrayList<>();
        creditValues.add(creditMap.get(Constants.CLASS_TYPE[0]));
        creditValues.add(creditMap.get(Constants.CLASS_TYPE[1]));
        creditValues.add(creditMap.get(Constants.CLASS_TYPE[2]));
        creditValues.add(creditMap.get(Constants.CLASS_TYPE[3]));
        creditValues.add(creditMap.get(Constants.CLASS_TYPE[4]));
        creditValues.add(creditMap.get(Constants.CLASS_TYPE[5]));

        return creditValues;
    }

    /**
     * 生成{@link net.muxi.huashiapp.ui.score.fragments.CurCreditFragment } 中二级列表中的group 名称
     * @return 填充成功的list
     */
    public static List<String> getCreditTypes(){
        List<String> classType = new ArrayList<>();
        classType.add("专业主干课程");
        classType.add("个性发展课程");
        classType.add("通识核心课");
        classType.add("通识必修课");
        classType.add("通识选修课");
        classType.add("其他课程");

        return classType;
    }

    /**
     * 生成和类型对应的课程的list
     * @param map
     * @return
     */
    public static List<List<Score>> getTypeOrderedList(HashMap<String,List<Score>> map){
        List<List<Score>> list = new ArrayList<>();
        if(map.get(Constants.CLASS_TYPE[0]) != null){
            list.add(map.get(Constants.CLASS_TYPE[0]));
        }else{
            list.add(new ArrayList<>());
        }
        if(map.get(Constants.CLASS_TYPE[1]) != null){
            list.add(map.get(Constants.CLASS_TYPE[1]));
        }else{
            list.add(new ArrayList<>());
        }

        if(map.get(Constants.CLASS_TYPE[2]) != null){
            list.add(map.get(Constants.CLASS_TYPE[2]));
        }else{
            list.add(new ArrayList<>());
        }

        if(map.get(Constants.CLASS_TYPE[3]) != null){
            list.add(map.get(Constants.CLASS_TYPE[3]));
        }else{
            list.add(new ArrayList<>());
        }

        if(map.get(Constants.CLASS_TYPE[4]) != null){
            list.add(map.get(Constants.CLASS_TYPE[4]));
        }else{
            list.add(new ArrayList<>());
        }

        if(map.get(Constants.CLASS_TYPE[5]) != null){
            list.add(map.get(Constants.CLASS_TYPE[5]));
        }else{
            list.add(new ArrayList<>());
        }

        return list;
    }

    /**
     *
     * @param start 选择开始年份
     * @param end   选择结束年份
     * @return list from start to end
     */
    public static List<String> parseNumber2Years(int start,int end){
        List<String> years=  new ArrayList<>();
        for(int i = start;i< end;i++) {
            years.add(String.valueOf(i));
        }
        return years;
    }
}
