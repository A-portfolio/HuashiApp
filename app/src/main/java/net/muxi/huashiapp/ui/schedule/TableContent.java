package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;


import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.util.DimensUtil;
import net.muxi.huashiapp.util.PreferenceUtil;
import net.muxi.huashiapp.util.TimeTableUtil;
import net.muxi.huashiapp.widget.TimeTable;

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

    public TableContent(Context context) {
        this(context, null);
    }

    public TableContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        mContext = context;
        this.setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制背景分割线
        Path path = new Path();
        for (int i = 0; i < 14; i++) {
            path.moveTo(0, TimeTable.COURSE_TIME_HEIGHT * (i + 1));
            path.lineTo(TimeTable.WEEK_DAY_WIDTH * 7, TimeTable.COURSE_TIME_HEIGHT * (i + 1));
        }
        for (int i = 0; i < 7; i++) {
            path.moveTo(TimeTable.WEEK_DAY_WIDTH * (i + 1), 0);
            path.moveTo(TimeTable.WEEK_DAY_WIDTH * (i + 1), TimeTable.COURSE_TIME_HEIGHT * 14);
        }
        Paint p = new Paint();
        p.setColor(getResources().getColor(R.color.divider));
        p.setStrokeWidth(DimensUtil.dp2px(1));
        p.setAntiAlias(true);
        canvas.drawPath(path, p);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

    }

    /**
     * 添加课程
     *
     * @param courseList 有效显示的课程，本周课程和非本周但与本周课程不冲突的课程
     */
    public void addCourses(List<Course> courseList) {
        mCourseList.addAll(courseList);
        for (int i = 0; i < courseList.size(); i++) {
            addCourseView(courseList.get(i));
        }
    }

    public void addCourseView(Course course) {
        TextView tvCourse = new TextView(mContext);
        LayoutParams courseParams = new LayoutParams(TimeTable.COURSE_WIDTH,
                course.getDuring() * TimeTable.COURSE_TIME_HEIGHT - 3);
        courseParams.setMargins(
                DimensUtil.dp2px(65 * TimeTableUtil.weekday2num(course.getDay()) + 1),
                DimensUtil.dp2px(57 * (course.getStart() - 1) + 1), 0, 0);
        tvCourse.setBackground(getResources().getDrawable(
                TimeTableUtil.getCourseBg(course.getColor(), 0)));
        tvCourse.setText(ellipseCourse(course.getCourse()) + "\n\n@" +
                course.getPlace() + "\n" +
                course.getTeacher());
        this.addView(tvCourse, courseParams);
        tvCourse.setOnClickListener(v -> {
            if (mOnCourseClickListener != null){
                mOnCourseClickListener.onCourseClick(course);
            }
        });
    }

    public void setOnCourseClickListener(OnCourseClickListener courseClickListener){
        mOnCourseClickListener = courseClickListener;
    }

    public void updateCourses(List<Course> courseList) {
        this.removeAllViews();
        addCourses(courseList);
    }

    public String ellipseCourse(String course){
        if (course.length() > 8){
            return course.substring(0,7) + "...";
        }
        return course;
    }
}
