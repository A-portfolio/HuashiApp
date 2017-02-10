package net.muxi.huashiapp.common.data;

/**
 * Created by ybao on 16/6/25.
 */
public class Scores {

    /**
     * category : 专业课
     * credit : 1.0
     * grade : 69.5
     * jxb_id : 2681122D7EBD18E8E0531E50A8C0CCFD
     * course : 信息管理与信息系统专业英语
     * type : 文
     *
     */

    private String category;
    private String credit;
    private String grade;
    private String jxb_id;
    private String course;
    private String type;
    private String kcxzmc;

    public String getKcxzmc() {
        return kcxzmc;
    }

    public void setKcxzmc(String kcxzmc) {
        this.kcxzmc = kcxzmc;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getJxb_id() {
        return jxb_id;
    }

    public void setJxb_id(String jxb_id) {
        this.jxb_id = jxb_id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
