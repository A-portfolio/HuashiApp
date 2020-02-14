package com.muxistudio.appcommon.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ybao on 16/7/30.
 */
public class Course implements Parcelable {

    //id :自定义课程储存id 我们这边在匣子2.1.8 版本之后不再上传 id 从服务器拉回来的数据服务器会给一个id!
    public String id;
    public String course;
    public String teacher;
    public List<Integer> weeks;
    public String day;
    public int start;
    public int during;
    public String place;
    public String remind;
    public int color;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.course);
        dest.writeString(this.teacher);
        dest.writeList(this.weeks);
        dest.writeString(this.day);
        dest.writeInt(this.start);
        dest.writeInt(this.during);
        dest.writeString(this.place);
        dest.writeString(this.remind);
        dest.writeInt(this.color);
    }

    public Course() {
    }

    protected Course(Parcel in) {
        this.id = in.readString();
        this.course = in.readString();
        this.teacher = in.readString();
        this.weeks = in.readArrayList(Integer.class.getClassLoader());
        this.day = in.readString();
        this.start = in.readInt();
        this.during = in.readInt();
        this.place = in.readString();
        this.remind = in.readString();
        this.color = in.readInt();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel source) {
            return new Course(source);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public boolean hasNullValue() {
      return //TextUtils.isEmpty(id)
           TextUtils.isEmpty(course)
          || TextUtils.isEmpty(teacher)
          || weeks.size() == 0 //TextUtils.isEmpty(weeks)
          || TextUtils.isEmpty(day)
          || TextUtils.isEmpty(place)
          || TextUtils.isEmpty(remind);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public List<Integer> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<Integer> weeks) {
        this.weeks = weeks;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getDuring() {
        return during;
    }

    public void setDuring(int during) {
        this.during = during;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public static Creator<Course> getCREATOR() {
        return CREATOR;
    }

    public static List<Integer> convertWeeks (String weeks) {
        List<Integer> weekList = new ArrayList<>();
        if ( weeks.charAt(0) == '[' )
            weeks = weeks.substring(1,weeks.length()-1);
        weeks = weeks.replaceAll(" ","");
        String[] week = weeks.split(",");
        for ( int i = 0 ; i < week.length ; i++ )
            weekList.add(Integer.parseInt(week[i]));
        return weekList;
    }

    public static String listToString(List<Integer> list) {
        String t = list.toString();
        t = t.replaceAll(" ","");
        return t.substring(1,t.length()-1);
    }
}
