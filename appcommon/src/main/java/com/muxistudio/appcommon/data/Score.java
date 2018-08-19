package com.muxistudio.appcommon.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ybao on 16/6/25.
 */
public class Score implements Parcelable {

    /**
     * category : 专业课
     * credit : 1.0
     * grade : 69.5
     * jxb_id : 2681122D7EBD18E8E0531E50A8C0CCFD
     * course : 信息管理与信息系统专业英语
     * type : 文
     *
     */

    public String category;
    public String credit;
    public String grade;
    public String jxb_id;
    public String course;
    public String type;
    public String kcxzmc;
    public String usual;
    public String ending;

    protected Score(Parcel in) {
        category = in.readString();
        credit = in.readString();
        grade = in.readString();
        jxb_id = in.readString();
        course = in.readString();
        type = in.readString();
        kcxzmc = in.readString();
        usual = in.readString();
        ending = in.readString();
    }

    public static final Creator<Score> CREATOR = new Creator<Score>() {
        @Override
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }

        @Override
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(credit);
        dest.writeString(grade);
        dest.writeString(jxb_id);
        dest.writeString(course);
        dest.writeString(type);
        dest.writeString(kcxzmc);
        dest.writeString(usual);
        dest.writeString(ending);
    }


}
