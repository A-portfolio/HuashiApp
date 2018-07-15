package com.muxistudio.appcommon.event;

import com.muxistudio.appcommon.data.Course;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/2/26
 */

public class DeleteCourseOkEvent {

    private Course mCourse;

    public DeleteCourseOkEvent(Course course) {
        mCourse = course;
    }

    public Course getCourse() {
        return mCourse;
    }

    public void setCourse(Course course) {
        mCourse = course;
    }
}
