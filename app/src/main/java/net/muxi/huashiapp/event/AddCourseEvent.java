package net.muxi.huashiapp.event;

import net.muxi.huashiapp.common.data.Course;

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
