package net.muxi.huashiapp.common.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.util.DateUtil;
import net.muxi.huashiapp.common.util.DimensUtil;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.TimeTableUtil;
import net.muxi.huashiapp.schedule.ScheduleTimeLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ybao on 16/4/19.
 * 显示课表,星期,节数的类
 */
public class TimeTable extends FrameLayout {

    public static final int WEEK_DAY_WIDTH = DimensUtil.dp2px(60);
    public static final int COURSE_TIME_HEIGHT = DimensUtil.dp2px(120);
    public static final int LITTLE_VIEW_WIDTH = DimensUtil.dp2px(50);
    public static final int LITTLE_VIEW_HEIGHT = DimensUtil.dp2px(40);
    public static final int COURSE_TIME_DIVIDER = DimensUtil.dp2px(2);

    private ScheduleTimeLayout mScheduleLayout;
    private FrameLayout[] dayCourseLayout;

    private Context mContext;

    List<String> weekDates;

    private String[] weekdays;

    private View view;
    private ScheduleTimeLayout mCourseLayout;
    private LinearLayout[] mCourseUnitLayout;
    private TextView[] mCourseTimeTv;
    private TextView[] mCourseNumTv;

    private ScheduleTimeLayout mWeekDayLayout;
    private LinearLayout[] mWeekdayUnitLayout;
    private TextView[] mWeekdayTv;
    private TextView[] mWeekdayDateTv;

    private float mx, my;
    private float curX, curY;
    private float startX, startY;
    private Date date1;
    private boolean isTouchCancel = true;

    private int type = 0;

    private OnLongPressedListenr mOnLongPressedListener;
    private OnCourseClickListener mOnCourseClickListener;


    public interface OnLongPressedListenr {
        void onLongPressed(Course course);
    }

    //监听点击课程
    public interface OnCourseClickListener {
        void onCourseClick(Course course);
    }

    public TimeTable(Context context) {
        this(context, null);
    }

    public TimeTable(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initLayout(context);
    }

    public void initLayout(Context context) {
        setupScrollView(context);
        setupWeekDayLayout(context);
        setupCourseTimeLayout(context);

        setLittleView(context);

    }

    public void setLittleView(Context context) {
        view = new View(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LITTLE_VIEW_WIDTH, LITTLE_VIEW_HEIGHT);
        view.setLayoutParams(params);
        view.setBackgroundColor(getResources().getColor(R.color.course_week_layout_bg));
        addView(view);
    }

    public void setupCourseTimeLayout(Context context) {

        mCourseLayout = new ScheduleTimeLayout(context);
        FrameLayout.LayoutParams courseLayoutParams = new
                FrameLayout.LayoutParams(LITTLE_VIEW_WIDTH, COURSE_TIME_HEIGHT * 7);
        courseLayoutParams.setMargins(0, LITTLE_VIEW_HEIGHT, 0, 0);
        mCourseLayout.setOrientation(LinearLayout.VERTICAL);
        addView(mCourseLayout, courseLayoutParams);
        mCourseUnitLayout = new LinearLayout[14];
        LinearLayout.LayoutParams unitLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                COURSE_TIME_HEIGHT / 2
        );
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0
        );
        tvParams.weight = 1;
        mCourseNumTv = new TextView[14];
        mCourseTimeTv = new TextView[14];
        ViewGroup.LayoutParams dividerParams = new
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                COURSE_TIME_DIVIDER);

        for (int i = 0; i < 14; i++) {
            mCourseUnitLayout[i] = new LinearLayout(mContext);
            mCourseUnitLayout[i].setGravity(Gravity.CENTER);
            mCourseUnitLayout[i].setOrientation(LinearLayout.VERTICAL);
            mCourseUnitLayout[i].setLayoutParams(unitLayoutParams);
            mCourseUnitLayout[i].setBackgroundColor(getResources().getColor(R.color.colorCourseTimeLayout));
            mCourseLayout.addView(mCourseUnitLayout[i]);

            String hour = "" + (i / 2 * 2 + 8);
            String minute;
            if (i % 2 == 0) {
                minute = "00";
            } else minute = "" + 55;
            mCourseNumTv[i] = new TextView(context);
            mCourseNumTv[i].setTextSize(18);
            mCourseNumTv[i].setTextColor(getResources().getColor(R.color.colorPrimary));
            mCourseNumTv[i].setText("" + (i + 1));
            mCourseTimeTv[i] = new TextView(mContext);
            mCourseTimeTv[i].setText(hour + ":" + minute);
            mCourseTimeTv[i].setGravity(Gravity.BOTTOM);
            mCourseUnitLayout[i].addView(mCourseTimeTv[i], tvParams);
            mCourseUnitLayout[i].addView(mCourseNumTv[i], tvParams);

            View divider = new View(context);
            divider.setBackgroundColor(mContext.getResources().getColor(R.color.divider_course));
            divider.setLayoutParams(dividerParams);
            mCourseUnitLayout[i].addView(divider);
            Logger.d(mCourseUnitLayout[i].getMeasuredHeight() + "");

        }

        Logger.d(mCourseLayout.getHeight() + "");

    }


    public void setupWeekDayLayout(Context context) {
        mWeekDayLayout = new ScheduleTimeLayout(context);
        FrameLayout.LayoutParams weekDayParams = new
                FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LITTLE_VIEW_HEIGHT);
        mWeekDayLayout.setLayoutParams(weekDayParams);
        mWeekDayLayout.setPadding(LITTLE_VIEW_WIDTH, 0, 0, 0);
        mWeekDayLayout.setBackgroundColor(getResources().getColor(R.color.course_week_layout_bg));
        mWeekDayLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(mWeekDayLayout, weekDayParams);

        mWeekdayUnitLayout = new LinearLayout[7];
        LinearLayout.LayoutParams unitParams = new LinearLayout.LayoutParams(
                WEEK_DAY_WIDTH,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        mWeekdayDateTv = new TextView[7];
        mWeekdayTv = new TextView[7];

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        weekDates = DateUtil.getTheWeekDate(0);
        weekdays = getResources().getStringArray(R.array.week_day);
        for (int i = 0; i < 7; i++) {
            mWeekdayUnitLayout[i] = new LinearLayout(mContext);
            mWeekdayUnitLayout[i].setLayoutParams(unitParams);
            mWeekdayUnitLayout[i].setOrientation(LinearLayout.VERTICAL);
            mWeekdayUnitLayout[i].setGravity(Gravity.CENTER);
            mWeekDayLayout.addView(mWeekdayUnitLayout[i]);

            mWeekdayDateTv[i] = new TextView(mContext);
            mWeekdayDateTv[i].setLayoutParams(params);
            mWeekdayDateTv[i].setText(weekDates.get(i));
            mWeekdayDateTv[i].setTextSize(12);
            mWeekdayDateTv[i].setTextColor(getResources().getColor(R.color.colorPrimary));
            mWeekdayTv[i] = new TextView(context);
            mWeekdayTv[i].setLayoutParams(params);
            mWeekdayTv[i].setGravity(Gravity.CENTER);
            mWeekdayTv[i].setText(weekdays[i]);
            mWeekdayUnitLayout[i].addView(mWeekdayTv[i]);
            mWeekdayUnitLayout[i].addView(mWeekdayDateTv[i]);
        }
    }

    //更改对应周的日期,传入的distance为选择周距当前周的周数
    public void changeTheDate(int distance) {
        weekDates = DateUtil.getTheWeekDate(distance);
        for (int i = 0; i < 7; i++) {
            mWeekdayDateTv[i].setText(weekDates.get(i));
        }
    }


    public void setupScrollView(Context context) {

        mScheduleLayout = new ScheduleTimeLayout(context);

        FrameLayout.LayoutParams scheduleLayoutParams = new
                FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, COURSE_TIME_HEIGHT * 7);
        scheduleLayoutParams.setMargins(LITTLE_VIEW_WIDTH, LITTLE_VIEW_HEIGHT, 0, 0);
        mScheduleLayout.setLayoutParams(scheduleLayoutParams);

        addView(mScheduleLayout, scheduleLayoutParams);
        dayCourseLayout = new FrameLayout[7];
        FrameLayout.LayoutParams relativeParams = new FrameLayout.LayoutParams(
                WEEK_DAY_WIDTH,
                COURSE_TIME_HEIGHT * 7
        );

        View[] views = new View[14];

        for (int i = 0; i < 7; i++) {
            dayCourseLayout[i] = new FrameLayout(context);
            dayCourseLayout[i].setLayoutParams(relativeParams);
            mScheduleLayout.addView(dayCourseLayout[i]);

            FrameLayout.LayoutParams[] dividerParams = new LayoutParams[14];

            for (int j = 0; j < 14; j++) {
                dividerParams[j] = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        DimensUtil.dp2px(2)
                );
                dividerParams[j].setMargins(0, COURSE_TIME_HEIGHT / 2 * (j + 1) - DimensUtil.dp2px(2), 0, 0);
                views[j] = new View(mContext);
                views[j].setBackgroundColor(getResources().getColor(R.color.divider_course));
                dayCourseLayout[i].addView(views[j], dividerParams[j]);
            }

        }

    }

    /**
     * 今天的那一列宽度增大
     *
     * @param today 今天是一周的第几天
     */
    public void setTodayLayout(int today) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                WEEK_DAY_WIDTH * 2,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        mWeekdayUnitLayout[today - 1].setLayoutParams(layoutParams);
        dayCourseLayout[today - 1].setLayoutParams(layoutParams);
        this.invalidate();
    }

    public void resetTodayLayout(int today) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                WEEK_DAY_WIDTH,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        mWeekdayUnitLayout[today - 1].setLayoutParams(layoutParams);
        dayCourseLayout[today - 1].setLayoutParams(layoutParams);
        this.invalidate();
    }

    //监听某些课程是否被长按,要删课
    public void setOnLongPressedListener(OnLongPressedListenr longPressedListner) {
        this.mOnLongPressedListener = longPressedListner;
    }

    public void setOnCourseClickListener(OnCourseClickListener courseClickListener) {
        mOnCourseClickListener = courseClickListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

//            this.addTouchables();
            case MotionEvent.ACTION_DOWN:
                mx = event.getX();
                my = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                curX = event.getX();
                curY = event.getY();
                mWeekDayLayout.scrollBy((int) (mx - curX), 0, type);
                mScheduleLayout.scrollBy((int) (mx - curX), (int) (my - curY), type);
                mCourseLayout.scrollBy(0, (int) (my - curY), type);
                mx = curX;
                my = curY;
                break;
            case MotionEvent.ACTION_UP:
                curX = event.getX();
                curY = event.getY();
                break;
        }

        return true;
    }

    /**
     * 设置在那种类型下的滑动
     *
     * @param type curweek : 0,  otherweek : 1
     */
    public void setType(int type) {
        this.type = type;
    }

    //添加每节课程的TextView
    public void setCourse(final List<Course> courses, int week) {
        Logger.d(courses.size() + "   week");
        List<Course> priorityCourses = getPriorityCourses(courses, week);
        List<Course> curCourses = findCurWeekCourses(courses, week);
        List<Course> otherCourses = getOtherCourses(courses, week);
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourse() != null && courses.get(i).getCourse().equals("re:从零开始的异世界生活")) {
                Logger.d(courses.get(i).getCourse());
                continue;
            } else {
                final int curCourse = i;
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        courses.get(i).getDuring() * COURSE_TIME_HEIGHT / 2 - 2
                );

                params.setMargins(0, COURSE_TIME_HEIGHT / 2 * (courses.get(i).getStart() - 1), 0, 0);
                final TextView courseTv = new TextView(mContext);
                if (curCourses.contains(courses.get(i))) {
                    courseTv.setBackground(getResources().getDrawable(TimeTableUtil.getCourseBg(courses.get(i).getColor())));
                } else if (priorityCourses.contains(courses.get(i))) {
                    courseTv.setBackground(getResources().getDrawable(TimeTableUtil.getCourseBg(courses.get(i).getColor())));
                } else if (otherCourses.contains(courses.get(i))) {
                    courseTv.setBackground(getResources().getDrawable(R.drawable.shape_rectangle_grey));
                }
                courseTv.setTextColor(Color.WHITE);
                String courseName = simplifyCourse(courses.get(i).getCourse());
                courseTv.setGravity(Gravity.CENTER_HORIZONTAL);
                if (priorityCourses.contains(courses.get(i)) || curCourses.contains(courses.get(i))) {
                    courseTv.setText(courseName + "\n@" +
                            courses.get(i).getPlace() + "\n" +
                            courses.get(i).getTeacher());
                } else if (otherCourses.contains(courses.get(i))) {
                    courseTv.setText(courseName + "\n@" +
                            courses.get(i).getPlace() + "\n" +
                            courses.get(i).getTeacher() + "\n[非本周]");
                }

                courseTv.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, final MotionEvent event) {
                        int action = event.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                isTouchCancel = false;
                                mx = event.getRawX();
                                my = event.getRawY();
                                startX = mx;
                                startY = my;
                                date1 = new Date(System.currentTimeMillis());
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Logger.d("runnable run " + !isTouchCancel);
                                        if ((Math.abs(startX - mx) < 4) && (Math.abs(startY - my) < 4) && (!isTouchCancel)) {
                                            Logger.d("childview long click");
                                            if (mOnLongPressedListener != null) {
                                                Logger.d("childview long click");
                                                mOnLongPressedListener.onLongPressed(courses.get(curCourse));
                                            }
                                        }
                                    }
                                }, 1000);
                                Logger.d("child view down");
                                break;
                            case MotionEvent.ACTION_MOVE:
                                curX = event.getRawX();
                                curY = event.getRawY();
                                mWeekDayLayout.scrollBy((int) (mx - curX), 0, type);
                                mScheduleLayout.scrollBy((int) (mx - curX), (int) (my - curY), type);
                                mCourseLayout.scrollBy(0, (int) (my - curY), type);
                                mx = curX;
                                my = curY;
                                break;
                            case MotionEvent.ACTION_UP:
                                curX = event.getRawX();
                                curY = event.getRawY();
                                Date date2 = new Date(System.currentTimeMillis());
                                if (DateUtil.getSecondSpace(date1, date2) < 1000 &&
                                        (Math.abs(startX - curX) < 4) && (Math.abs(startY - curY) < 4)) {
                                    if (mOnCourseClickListener != null) {
                                        Logger.d("child view click");
                                        mOnCourseClickListener.onCourseClick(courses.get(curCourse));

                                    }
                                }
                                isTouchCancel = true;
                                break;

                        }
                        return true;
                    }
                });

                int j;
                j = transDaysToInt(courses.get(i).getDay());
                dayCourseLayout[j].addView(courseTv, params);
            }
        }
    }

    private List<Course> getOtherCourses(List<Course> courses, int week) {
        List<Course> otherCourses = new ArrayList<>();
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (!TimeTableUtil.isThisWeek(week, course.getWeeks())) {
                boolean isConflict = false;
                for (int j = 0; j < courses.size(); j++) {
                    if (j == i) {
                        continue;
                    }
                    if (course.getDay().equals(courses.get(j).getDay()) && course.getStart() == courses.get(j).getStart() && course.getDuring() == courses.get(j).getDuring()) {
                        isConflict = true;
                    }
                }
                if (!isConflict) {
                    otherCourses.add(course);
                    Logger.d(course.getCourse());
                }
            }
        }
        return otherCourses;
    }

    //获取当前周的课程,不包括优先显示的课程
    private List<Course> findCurWeekCourses(List<Course> courses, int week) {
        List<Course> curWeekCourses = new ArrayList<>();
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (TimeTableUtil.isThisWeek(week, course.getWeeks())) {
                boolean isConflict = false;
                for (int j = 0; j < courses.size(); j++) {
                    if (j == i) {
                        continue;
                    }
                    if (course.getDay().equals(courses.get(j).getDay()) && course.getStart() == courses.get(j).getStart() && course.getDuring() == courses.get(j).getDuring()) {
                        isConflict = true;
                    }
                }
                if (!isConflict) {
                    curWeekCourses.add(course);
                    Logger.d(course.getCourse());
                }
            }
        }
        return curWeekCourses;
    }

    //获取优先显示的课程,只包括冲突的课程
    private List<Course> getPriorityCourses(List<Course> courses, int week) {

        List<Course> priorityCourses = new ArrayList<>();
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            boolean isConflict = false;
            for (int j = i + 1; j < courses.size(); j++) {
                if (course.getDay().equals(courses.get(j).getDay()) && course.getStart() == courses.get(j).getStart() && course.getDuring() == courses.get(j).getDuring()) {
                    isConflict = true;
                    if (TimeTableUtil.isThisWeek(week, courses.get(j).getWeeks())) {
                        course = courses.get(j);
                    }
                }
            }
            if (isConflict) {
                priorityCourses.add(course);
                Logger.d(course.getCourse());
            }
        }
        return priorityCourses;
    }

    /**
     * 移动 中间的timetable
     *
     * @param x
     * @param y
     */

    public void scrollScheduleLayout(int x, int y) {
        mScheduleLayout.scrollTo(x, y);
        mWeekDayLayout.scrollTo(x, y);
        mCourseLayout.scrollTo(x, y);
    }
//    private List<Course> removeRepeatedCourses(List<Course> courses) {
//        for (int i = 0,j = courses.size();i < j;i ++){
//            Course course = courses.get(i);
//            courses.indexOf()
//        }
//    }


    private String simplifyCourse(String course) {
        if (course.length() > 12) {
            return course.substring(0, 11) + "...";
        } else {
            return course;
        }
    }


    //将星期转换为 int类型`
    private int transDaysToInt(String s) {
        int i;
        switch (s) {
            case "星期一":
                i = 0;
                break;
            case "星期二":
                i = 1;
                break;
            case "星期三":
                i = 2;
                break;
            case "星期四":
                i = 3;
                break;
            case "星期五":
                i = 4;
                break;
            case "星期六":
                i = 5;
                break;
            case "星期日":
                i = 6;
                break;
            default:
                i = 6;
                break;
        }
        return i;
    }

    //删除所有课,在更新课程表视图时开始调
    public void removeCourse() {
        for (int i = 0; i < 7; i++) {
            if (dayCourseLayout[i].getChildCount() > 0) {
                dayCourseLayout[i].removeAllViews();
            }

            View[] views = new View[14];
            FrameLayout.LayoutParams[] dividerParams = new LayoutParams[14];

            for (int j = 0; j < 14; j++) {
                if (dividerParams[j] == null) {
                    dividerParams[j] = new LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            DimensUtil.dp2px(2)
                    );
                    dividerParams[j].setMargins(0, COURSE_TIME_HEIGHT / 2 * (j + 1) - DimensUtil.dp2px(2), 0, 0);
                }
                views[j] = new View(mContext);
                views[j].setBackgroundColor(getResources().getColor(R.color.divider_course));
                dayCourseLayout[i].addView(views[j], dividerParams[j]);
            }

        }
    }

}
