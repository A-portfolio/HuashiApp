package net.muxi.huashiapp.common.data;

/**
 * Created by ybao on 16/6/25.
 */
public class Scores {

    /**
     * total : 77.5
     * ending : 70
     * usual :
     */

    private GradeBean grade;
    /**
     * grade : {"total":"77.5","ending":"70","usual":""}
     * course : 管理运筹学
     * category : 专业课
     * type : 理
     * credit : 3.0
     */

    private String course;
    private String category;
    private String type;
    private String credit;

    public GradeBean getGrade() {
        return grade;
    }

    public void setGrade(GradeBean grade) {
        this.grade = grade;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public static class GradeBean {
        private String total;
        private String ending;
        private String usual;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getEnding() {
            return ending;
        }

        public void setEnding(String ending) {
            this.ending = ending;
        }

        public String getUsual() {
            return usual;
        }

        public void setUsual(String usual) {
            this.usual = usual;
        }
    }
}
