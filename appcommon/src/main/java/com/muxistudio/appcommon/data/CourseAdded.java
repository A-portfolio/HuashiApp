package com.muxistudio.appcommon.data;

import java.util.List;

public class CourseAdded {

    /**
     * course : 大学体育4
     * teacher : 覃凤珍
     * place : 网球场
     * start : 5
     * during : 2
     * day : 1
     * weeks : [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17]
     */

    private String course;
    private String teacher;
    private String place;
    private String start;
    private String during;
    private String day;
    private List<Integer> weeks;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDuring() {
        return during;
    }

    public void setDuring(String during) {
        this.during = during;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<Integer> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<Integer> weeks) {
        this.weeks = weeks;
    }

    public static CourseAdded convert(Course course ) {

        CourseAdded courseAdded = new CourseAdded();

        courseAdded.course = course.course;
        courseAdded.teacher = course.teacher;
        courseAdded.place = course.place;
        courseAdded.start = course.start+"";
        courseAdded.during = course.during+"";
        courseAdded.day = course.day;
        courseAdded.weeks = course.weeks;

        return courseAdded;
    }

}
