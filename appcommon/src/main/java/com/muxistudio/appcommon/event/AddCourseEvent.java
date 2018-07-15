package com.muxistudio.appcommon.event;

import com.muxistudio.appcommon.data.Course;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/3/2
 */

public class AddCourseEvent {

    public Course course;

    public AddCourseEvent(Course course){
        this.course = course;
    }
}
