package net.muxi.huashiapp.ui.schedule;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kolibreath on 18-2-2.
 */
//这个是一个很大很大的list
public class AuditCourse {
    private List<ResBean> res;
    //获取(上课的时间和上课的节数)和(上课的周数)
    public static String[] getCourseTime(String when){
      String s = "((.*)\\{(.*)\\})";
      Pattern pattern = Pattern.compile(s);
      Matcher m = pattern.matcher(when);
      String pieces [] = new String[2] ;
      while(m.find()){
          pieces[0] = m.group(2);
          pieces[1] = m.group(3);
      }
      return pieces;
    }

    public List<ResBean> getRes() {
        return res;
    }

    public void setRes(List<ResBean> res) {
        this.res = res;
    }

    public static class ResBean {
        /**
         * name : 高级语言程序设计
         * teacher : 魏开平
         * ww : [{"where":"9201.0","when":"星期一第7-8节{1-17周(单)}"}]
         * forwho : 计算机类
         * rank : 2017
         * kind : 专业课
         * no : 48740002.0
         */

        private String name;
        private String teacher;
        private String forwho;
        private int rank;
        private String kind;
        private String no;
        private List<WwBean> ww;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTeacher() {
            return teacher;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }

        public String getForwho() {
            return forwho;
        }

        public void setForwho(String forwho) {
            this.forwho = forwho;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public List<WwBean> getWw() {
            return ww;
        }

        public void setWw(List<WwBean> ww) {
            this.ww = ww;
        }

        public static class WwBean {
            /**
             * where : 9201.0
             * when : 星期一第7-8节{1-17周(单)}
             */

            private String where;
            private String when;

            public String getWhere() {
                return where;
            }

            public void setWhere(String where) {
                this.where = where;
            }

            public String getWhen() {
                return when;
            }

            public void setWhen(String when) {
                this.when = when;
            }
        }
    }
}
