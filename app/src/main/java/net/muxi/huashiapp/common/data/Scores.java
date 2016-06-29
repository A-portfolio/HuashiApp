package net.muxi.huashiapp.common.data;

/**
 * Created by ybao on 16/6/25.
 */
public class Scores {

    /**
     * grade : 83.0
     * course : 大学体育（4）（武术）
     * category : null
     * credit : 1.0
     */

    private String grade;
    private String course;
    private Object category;
    private String credit;

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}
