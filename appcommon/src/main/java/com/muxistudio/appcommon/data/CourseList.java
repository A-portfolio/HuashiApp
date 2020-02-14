package com.muxistudio.appcommon.data;

import java.util.List;

public class CourseList {


    private int code;
    private String message;
    /**
     * data : {"table":[{"id":"0","course":"软件工程","teacher":"叶光辉","place":"N312","start":"1","during":"2","day":"1","source":"xk","weeks":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17],"remind":false,"color":0},{"id":"1","course":"管理信息系统","teacher":"李玉海","place":"N312","start":"5","during":"2","day":"1","source":"xk","weeks":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17],"remind":false,"color":1},{"id":"2","course":"面向对象程序设计（Java）","teacher":"王忠义","place":"N313","start":"7","during":"2","day":"1","source":"xk","weeks":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17],"remind":false,"color":2},{"id":"3","course":"信息管理与信息系统专业英语","teacher":"王晓","place":"N316","start":"1","during":"2","day":"2","source":"xk","weeks":[1,2,3,4,5,6,7,8,9],"remind":false,"color":3},{"id":"4","course":"信息系统项目管理","teacher":"胡伟雄","place":"N316","start":"3","during":"2","day":"2","source":"xk","weeks":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17],"remind":false,"color":0},{"id":"5","course":"生活中的公共关系学（通核）","teacher":"乔花芳","place":"9-13","start":"3","during":"2","day":"3","source":"xk","weeks":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17],"remind":false,"color":1},{"id":"6","course":"管理信息系统","teacher":"李玉海","place":"N221","start":"5","during":"2","day":"3","source":"xk","weeks":[1,3,5,7,9,11,13,15,17],"remind":false,"color":2},{"id":"7","course":"面向对象程序设计（Java）","teacher":"王忠义","place":"信管实验室","start":"7","during":"2","day":"3","source":"xk","weeks":[2,4,6,8,10,12,14,16],"remind":false,"color":3},{"id":"8","course":"管理运筹学","teacher":"董庆兴","place":"N316","start":"1","during":"2","day":"4","source":"xk","weeks":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17],"remind":false,"color":0},{"id":"9","course":"管理统计学","teacher":"刘向","place":"N316","start":"3","during":"2","day":"4","source":"xk","weeks":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17],"remind":false,"color":1},{"id":"10","course":"大学体育4","teacher":"刘卫民","place":"田径场11","start":"7","during":"2","day":"4","source":"xk","weeks":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16],"remind":false,"color":2},{"id":"11","course":"音乐与心理健康（通核）","teacher":"王晓蓉","place":"y727","start":"9","during":"2","day":"4","source":"xk","weeks":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17],"remind":false,"color":3}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<Course> table;

        public List<Course> getTable() {
            return table;
        }

        public void setTable(List<Course> table) {
            this.table = table;
        }
    }

}
