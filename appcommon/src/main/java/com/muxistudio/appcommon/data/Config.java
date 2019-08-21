package com.muxistudio.appcommon.data;

import java.util.List;

public class Config {


    /**
     * calendarUrl : http://ossccnubox2.muxixyz.com/2019-2020-fall-calendar.png
     * flashScreenUrl :
     * showGuisheng : false
     * startCountDayPreset : 2019-02-24
     * startCountDayPresetForV2 : 2019-02-24
     * updateInfo : 2.0版是一次全新的重写，修复了课程表的显示问题，提升了匣子的稳定性，增加了空闲教室和蹭课功能，请大家尽快升级
     * version : 2.0.0
     * shouldPullCourse : false
     * flashStartDay :
     * flashEndDay :
     * gradeJSUrl :
     * tableJSUrl :
     * rax : [{"key":"com.muxistudio.table.main","version":"2.0.5","url":"http://static.muxixyz.com/box/2.0.6/com.muxistudio.table.main.js"},{"key":"com.muxistudio.grade.result","version":"2.0.5","url":"http://static.muxixyz.com/box/2.0.5/com.muxistudio.grade.result.js"},{"key":"com.muxistudio.calendar","version":"2.0.1","url":"https://static.muxixyz.com/box/2.0.1/com.muxistudio.calendar.js"}]
     */

    private String calendarUrl;
    private String flashScreenUrl;
    private String showGuisheng;
    private String startCountDayPreset;
    private String startCountDayPresetForV2;
    private String updateInfo;
    private String version;
    private boolean shouldPullCourse;
    private String flashStartDay;
    private String flashEndDay;
    private String gradeJSUrl;
    private String tableJSUrl;
    private List<RaxBean> rax;

    public String getCalendarUrl() {
        return calendarUrl;
    }

    public void setCalendarUrl(String calendarUrl) {
        this.calendarUrl = calendarUrl;
    }

    public String getFlashScreenUrl() {
        return flashScreenUrl;
    }

    public void setFlashScreenUrl(String flashScreenUrl) {
        this.flashScreenUrl = flashScreenUrl;
    }

    public String getShowGuisheng() {
        return showGuisheng;
    }

    public void setShowGuisheng(String showGuisheng) {
        this.showGuisheng = showGuisheng;
    }

    public String getStartCountDayPreset() {
        return startCountDayPreset;
    }

    public void setStartCountDayPreset(String startCountDayPreset) {
        this.startCountDayPreset = startCountDayPreset;
    }

    public String getStartCountDayPresetForV2() {
        return startCountDayPresetForV2;
    }

    public void setStartCountDayPresetForV2(String startCountDayPresetForV2) {
        this.startCountDayPresetForV2 = startCountDayPresetForV2;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isShouldPullCourse() {
        return shouldPullCourse;
    }

    public void setShouldPullCourse(boolean shouldPullCourse) {
        this.shouldPullCourse = shouldPullCourse;
    }

    public String getFlashStartDay() {
        return flashStartDay;
    }

    public void setFlashStartDay(String flashStartDay) {
        this.flashStartDay = flashStartDay;
    }

    public String getFlashEndDay() {
        return flashEndDay;
    }

    public void setFlashEndDay(String flashEndDay) {
        this.flashEndDay = flashEndDay;
    }

    public String getGradeJSUrl() {
        return gradeJSUrl;
    }

    public void setGradeJSUrl(String gradeJSUrl) {
        this.gradeJSUrl = gradeJSUrl;
    }

    public String getTableJSUrl() {
        return tableJSUrl;
    }

    public void setTableJSUrl(String tableJSUrl) {
        this.tableJSUrl = tableJSUrl;
    }

    public List<RaxBean> getRax() {
        return rax;
    }

    public void setRax(List<RaxBean> rax) {
        this.rax = rax;
    }

    public static class RaxBean {
        /**
         * key : com.muxistudio.table.main
         * version : 2.0.5
         * url : http://static.muxixyz.com/box/2.0.6/com.muxistudio.table.main.js
         */

        private String key;
        private String version;
        private String url;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
