package net.muxi.huashiapp.ui.timeTable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Scroller;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.util.DimensUtil;
import net.muxi.huashiapp.util.TimeTableUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ybao on 17/1/27.
 */

//在这里设置是否有 非本周字段
public class TableContent extends FrameLayout {
    private Map<String ,List<String>> overlapMap = new HashMap<>();
    private List<String> overlapTags = new ArrayList<>();
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
        p.setAntiAlias(true);

        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        canvas.drawRect(0,0,TimeTable.WEEK_DAY_WIDTH * 7,TimeTable.COURSE_TIME_HEIGHT * 14,p);

        p.setColor(getResources().getColor(R.color.divider));
        p.setStrokeWidth(DimensUtil.dp2px(1));
        p.setStyle(Paint.Style.STROKE);
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
    public void smoothScrollTo(int x,int y){
        mScroller.startScroll(mScroller.getCurrX(),mScroller.getCurrY(),x,y);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
    /**
     * 添加课程
     *
     * @param courseList 有效显示的课程，本周课程和非本周但与本周课程不冲突的课程
     */
    public void addCourses(List<Course> courseList,int selectWeek) {
        mCourseList.addAll(courseList);
        //查有没有重叠的课程
        generateMap(courseList);
        for (int i = 0; i < courseList.size(); i++) {
            if (!TimeTableUtil.isThisWeek(selectWeek,courseList.get(i).weeks)) {
                    addCourseView(courseList.get(i),selectWeek,courseList.get(i).weeks);
            }
        }
        for (int i = 0;i < courseList.size();i ++){
            if (TimeTableUtil.isThisWeek(selectWeek,courseList.get(i).weeks)){
                    addCourseView(courseList.get(i),selectWeek,courseList.get(i).weeks);
            }
        }
    }

    private List<String> convert(String[] weeks){
        List<String > list = new ArrayList<>();
        for(int i=Integer.parseInt(weeks[0]);i<=Integer.parseInt(weeks[weeks.length-1]);i++){
            list.add(String.valueOf(i));
        }
        return list;
    }


    public void addCourseView(Course course,int selectWeek,String weekStr) {
        String tag = generateTag(course);
        String value = generateValue(course);

        List<String > weekList = convert(weekStr.split(","));
        CourseView tvCourse = new CourseView(mContext);
        checkOverLap(tag,value,tvCourse);

        tvCourse.setTextColor(Color.WHITE);
        tvCourse.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
        tvCourse.setPadding(DimensUtil.dp2px(10),DimensUtil.dp2px(8),DimensUtil.dp2px(10),DimensUtil.dp2px(8));
        LayoutParams courseParams = new LayoutParams(TimeTable.COURSE_WIDTH,
                course.during * TimeTable.COURSE_TIME_HEIGHT - DimensUtil.dp2px(3));
        courseParams.setMargins(DimensUtil.dp2px(65 * TimeTableUtil.weekday2num(course.day) + 1),
                DimensUtil.dp2px(57 * (course.start - 1) + 1), 0, 0);


        if (TimeTableUtil.isThisWeek(selectWeek,course.weeks)) {
            tvCourse.setBackground(getResources().getDrawable(TimeTableUtil.getCourseBgAccordDay(course.day)));
            tvCourse.setText(ellipseCourse(course.course) + "\n\n@" +
                    course.place + "\n" +
                    course.teacher);
        }else {
            tvCourse.setBackground(getResources().getDrawable(R.drawable.ripple_grey));
            tvCourse.setText(ellipseNotCurCourse(course.course) + "\n\n@" +
            course.place + "\n" +
            course.teacher);
        }
        tvCourse.setCourseId(course.id);
        this.addView(tvCourse, courseParams);



        //{4,5,6,7,8}
        //假设当前周为3 如果 list中开始的周数为list.get(0) 小于开始周 不会不显示 会显示灰色
        //假设当前周为10 如果list中开始的周数为list.get(0) 大于开始 但是不在 范围之内会设置不可见 避免重叠的情况
        if(selectWeek > Integer.parseInt(weekList.get(0))&&selectWeek<Integer.parseInt(weekList
        .get(weekList.size()-1))) {
            if (!weekList.contains(String.valueOf(selectWeek))) {
                overlapTags.add(tag);
                tvCourse.setVisibility(GONE);
            }
        }


        tvCourse.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                ((TimeTable) getParent().getParent()).setCurDownTarget(view);
            }
            return false;
        });

    }

    private void  generateMap(List<Course> courseList){
        for(Course course:courseList){
            overlapMap.put(generateTag(course),new ArrayList<>());
        }
        for(String key:overlapMap.keySet()){
            for(Course course:courseList){
                if(key.equals(generateTag(course))){
                    overlapMap.get(key).add(generateValue(course));
                }
            }
        }
    }

    //确定一种相同的生成tag的法则
    private String generateTag(Course course){
        return course.getDay()+course.getStart()+course.getDuring();
    }
    //确定一种相同的生成value的法则
    private String generateValue(Course course){
        return course.getDay()+course.getStart()+course.getDuring()+course.teacher;
    }

    //查看一下有没有重叠的课程
    private void checkOverLap(String tag,String value,CourseView courseview){
       if(overlapMap.get(tag).contains(value)&&overlapMap.get(tag).size()>1){
           courseview.setIsTipOn(true);
       }
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
