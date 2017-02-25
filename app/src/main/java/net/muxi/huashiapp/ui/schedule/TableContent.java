package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;
import android.widget.TextView;


import com.tencent.smtt.export.external.interfaces.IX5WebSettings;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.util.DimensUtil;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.TimeTableUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ybao on 17/1/27.
 */

public class TableContent extends FrameLayout {

    private Scroller mScroller;
    private Context mContext;

    private OnCourseClickListener mOnCourseClickListener;

    private List<Course> mCourseList = new ArrayList<>();
    
//    private TextView clickEventTarget = null;

    public TableContent(Context context) {
        this(context, null);
    }

    public TableContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        mContext = context;
        this.setBackgroundColor(Color.WHITE);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制背景分割线
        Path path = new Path();
        for (int i = 0; i < 14; i++) {
            path.moveTo(0, TimeTable.COURSE_TIME_HEIGHT * (i + 1) - DimensUtil.dp2px(0.5f));
            path.lineTo(TimeTable.WEEK_DAY_WIDTH * 7, TimeTable.COURSE_TIME_HEIGHT * (i + 1) - DimensUtil.dp2px(0.5f));
        }
        for (int i = 0; i < 7; i++) {
            path.moveTo(TimeTable.WEEK_DAY_WIDTH * (i + 1) - DimensUtil.dp2px(0.5f) , 0);
            path.lineTo(TimeTable.WEEK_DAY_WIDTH * (i + 1) - DimensUtil.dp2px(0.5f), TimeTable.COURSE_TIME_HEIGHT * 14);
        }
        Paint p = new Paint();
        p.setColor(getResources().getColor(R.color.divider));
        p.setStrokeWidth(DimensUtil.dp2px(1));
        p.setStyle(Paint.Style.STROKE);
        p.setAntiAlias(true);
        canvas.drawPath(path, p);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * 添加课程
     *
     * @param courseList 有效显示的课程，本周课程和非本周但与本周课程不冲突的课程
     */
    public void addCourses(List<Course> courseList,int selectWeek) {
        mCourseList.addAll(courseList);
        for (int i = 0; i < courseList.size(); i++) {
            if (!TimeTableUtil.isThisWeek(selectWeek,courseList.get(i).weeks)) {
                addCourseView(courseList.get(i),selectWeek);
            }
        }
        for (int i = 0;i < courseList.size();i ++){
            if (TimeTableUtil.isThisWeek(selectWeek,courseList.get(i).weeks)){
                addCourseView(courseList.get(i),selectWeek);
            }
        }
    }

    public void addCourseView(Course course,int selectWeek) {
        CourseView tvCourse = new CourseView(mContext);
        tvCourse.setTextColor(Color.WHITE);
        tvCourse.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
        tvCourse.setPadding(DimensUtil.dp2px(10),DimensUtil.dp2px(8),DimensUtil.dp2px(10),DimensUtil.dp2px(8));
        LayoutParams courseParams = new LayoutParams(TimeTable.COURSE_WIDTH,
                course.during * TimeTable.COURSE_TIME_HEIGHT - 3);
        courseParams.setMargins(
                DimensUtil.dp2px(65 * TimeTableUtil.weekday2num(course.day) + 1),
                DimensUtil.dp2px(57 * (course.start - 1) + 1), 0, 0);
        if (TimeTableUtil.isThisWeek(selectWeek,course.weeks)) {
            tvCourse.setBackground(getResources().getDrawable(
                    TimeTableUtil.getCourseBg(course.color)));
            tvCourse.setText(ellipseCourse(course.course) + "\n\n@" +
                    course.place + "\n" +
                    course.teacher);
        }else {
            tvCourse.setBackground(getResources().getDrawable(R.drawable.shape_grey));
            tvCourse.setText(ellipseNotCurCourse(course.course) + "\n\n@" +
            course.place + "\n" +
            course.teacher);
        }
        tvCourse.setCourseId(course.id);
        this.addView(tvCourse, courseParams);

        tvCourse.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                ((TimeTable) getParent().getParent().getParent()).setCurDownTarget(view);
            }
            return false;
        });
    }



    public void setOnCourseClickListener(OnCourseClickListener courseClickListener){
        mOnCourseClickListener = courseClickListener;
    }

    public void updateCourses(List<Course> courseList,int week) {
        this.removeAllViews();
        addCourses(courseList,week);
    }

    public String ellipseCourse(String course){
        if (course.length() > 8){
            return course.substring(0,7) + "...";
        }
        return course;
    }

    public String ellipseNotCurCourse(String course){
        String s = "非本周-";
        if (course.length() > 4){
            return s + course.substring(0,3) + "...";
        }
        return s + course;
    }
}
