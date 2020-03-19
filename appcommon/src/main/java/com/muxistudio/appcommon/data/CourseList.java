package com.muxistudio.appcommon.data;

import java.util.List;

public class CourseList {


    private int code;
    private String message;

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