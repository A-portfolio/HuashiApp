package net.muxi.huashiapp.utils;

import android.annotation.SuppressLint;

import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.data.Score;
import com.muxistudio.common.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
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
    public static String parseNames2String(List<String> termNames, char separator) {
        if (termNames.size() == 1)
            return termNames.get(0);
        String names = "";
        for (String name : termNames) {
            names = names + name + separator;
        }
        names = names.substring(0, names.length()-1);
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
            return String.format("%d-%d学年", start, end);
        }
        String start = years.get(0), end = years.get(years.size() - 1);
        return String.format("%s-%s", start, end);
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
    public static HashMap<String, Double> getSortedGroupCreditMap(List<Score> credits) {
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

                if(credit.kcxzmc == null){
                  double addUp = creditMap.get("其他") + Double.parseDouble(credit.credit);
                  creditMap.put("其他", addUp);
                  break;
                }
                if (type.equals(credit.kcxzmc)) {
                    double addUp = creditMap.get(type) + Double.parseDouble(credit.credit);
                    creditMap.put(type, addUp);
                    break;
                }
                //对于其他的组别额外考虑
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

            for (int i = 0; i < Constants.CLASS_TYPE.length; i++) {
                String type = Constants.CLASS_TYPE[i];
                List<Score> c ;
                //如果类型是null 的话直接加入不可分类的类型
                if(credit.kcxzmc == null ){
                  c = sortedMap.get(Constants.CLASS_TYPE[5]);
                  c.add(credit);
                  sortedMap.put(Constants.CLASS_TYPE[5],c);
                  break;
                }
                //如果类型1错误 比如有些通识核心变成了通识选修分类
                if(type.contains("通核")){
                  c = sortedMap.get(Constants.CLASS_TYPE[2]);
                  c.add(credit);
                  sortedMap.put(Constants.CLASS_TYPE[2],c);
                  break;
                }
                if(type.equals("专业选修课")){
                  c = sortedMap.get(Constants.CLASS_TYPE[1]);
                  c.add(credit);
                  sortedMap.put(Constants.CLASS_TYPE[1],c);
                  break;
                }

                if (type.equals(credit.kcxzmc)) {
                    c = sortedMap.get(type);
                    c.add(credit);
                    sortedMap.put(type,c);
                    break;
                }

                if(i== Constants.CLASS_TYPE.length - 1){
                    c = sortedMap.get(Constants.CLASS_TYPE[5]);
                    c.add(credit);
                    sortedMap.put(Constants.CLASS_TYPE[5],c);
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

    /**
     * 返回正确的年份截止 如果今年是2017 但是用户在选择年份的时候选择了2019 会自动过滤到2017
     * 但是如果年份相同，比如今年是2017，用户选择startYear 为2017 但是选择endYear为2019，
     * 如果再次定位到2017 会非常不合理，这个时候的查询年份为2017-2018
     *
     * 注意：为什么不在选择框中指定年份： 对于新生来看的话选择学年的Dialog会非常难看，所以在这里指定
     *
     * @Param startYear 起始年份
     * @param endYear   截止年份
     * @return startYear 一定小于 endYear
     */
    public static int getProperEndYear(int startYear, int endYear){
        int curYear = Integer.parseInt(DateUtil.getCurYear(new Date(System.currentTimeMillis())));

       //eg startYear = 2016 endYear = 2017 cureYear = 2016
        if(endYear > curYear && curYear == startYear){
            return (endYear);
        }

        //eg startYear = 2016 endYear = 2020 curYear = 2017
        if(endYear > curYear){
            return (curYear);
        }
        return (endYear);
    }

    /**
     * 计算用户所选学年的所有学分之和
     * @param groupCredit 所有列表的总学分之和
     * @return double 总学分之和
     */
    public static double getCreditTotal(HashMap<String,Double> groupCredit){
        double total = 0d;
        Set<String> keys = groupCredit.keySet();
        for(String key: keys) {
          if(key.equals(Constants.ALL_CREDIT))
            continue;
          total += groupCredit.get(key);
        }
        return total;
    }

}
