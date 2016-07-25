package net.muxi.huashiapp.common.widget;

import android.content.Context;
import android.graphics.Color;
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
import net.muxi.huashiapp.schedule.ScheduleActivity;
import net.muxi.huashiapp.schedule.ScheduleTimeLayout;

import java.util.List;
import java.util.Random;

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

    public static final int TOUCH_FLAG_EXTEND = 1;
    public static final int TOUCH_FLAG_BACK = 0;
    //初始的时候是查看其他周的 View 没有显示
    private int mTouchFlag = TOUCH_FLAG_BACK;

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

    private OnScrollBottomListener mScrollBottomListener;
    private OnLongPressedListenr mOnLongPressedListener;

    public interface OnScrollBottomListener {
        void onScrollBottom(boolean b);
    }

    public interface OnLongPressedListenr {
        void onLongPressed(Course course);
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
            divider.setBackgroundColor(mContext.getResources().getColor(R.color.divider_course_time));
            divider.setLayoutParams(dividerParams);
            mCourseUnitLayout[i].addView(divider);
            Logger.d(mCourseUnitLayout[i].getMeasuredHeight() + "");

        }

        Logger.d(mCourseLayout.getHeight() + "");

    }


    public void setupWeekDayLayout(Context context) {
        mWeekDayLayout = new ScheduleTimeLayout(context);
        FrameLayout.LayoutParams weekDayParams = new
                FrameLayout.LayoutParams(WEEK_DAY_WIDTH * 7, LITTLE_VIEW_HEIGHT);
        mWeekDayLayout.setLayoutParams(weekDayParams);
        mWeekDayLayout.setPadding(LITTLE_VIEW_WIDTH, 0, 0, 0);
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
                FrameLayout.LayoutParams(WEEK_DAY_WIDTH * 7, COURSE_TIME_HEIGHT * 7);
        scheduleLayoutParams.setMargins(LITTLE_VIEW_WIDTH, LITTLE_VIEW_HEIGHT, 0, 0);
        mScheduleLayout.setLayoutParams(scheduleLayoutParams);

        addView(mScheduleLayout, scheduleLayoutParams);
        dayCourseLayout = new FrameLayout[7];
        FrameLayout.LayoutParams relativeParams = new FrameLayout.LayoutParams(
                WEEK_DAY_WIDTH,
                COURSE_TIME_HEIGHT * 7
        );
        FrameLayout.LayoutParams dividerParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                DimensUtil.dp2px(2)
        );
        View[] views = new View[14];

        for (int i = 0; i < 7; i++) {
            dayCourseLayout[i] = new FrameLayout(context);
            dayCourseLayout[i].setLayoutParams(relativeParams);
            mScheduleLayout.addView(dayCourseLayout[i]);

            for (int j = 0; j < 14; j++) {
                dividerParams.setMargins(0, COURSE_TIME_HEIGHT / 2 * (j + 1), 0, 0);
                views[j] = new View(mContext);
                dayCourseLayout[i].addView(views[j], dividerParams);
            }

        }

    }


    public void setOnScrollBottomListener(OnScrollBottomListener onScrollBottomListener) {
        mScrollBottomListener = onScrollBottomListener;
    }

    //监听某些课程是否被长按,用户可能要删课
    public void setOnLongPressedListener(OnLongPressedListenr longPressedListner) {
        this.mOnLongPressedListener = longPressedListner;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mx = event.getX();
                my = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                curX = event.getX();
                curY = event.getY();

                //当课程表滑至底部时直接收回已展开的选择第几周,
                if ((mScheduleLayout.getScrollY() + my - curY) >
                        (TimeTable.COURSE_TIME_HEIGHT * 7 - DimensUtil.getScreenHeight() + ScheduleActivity.SELECT_WEEK_LAYOUT_HEIGHT
                                + DimensUtil.getActionbarHeight() + DimensUtil.getStatusBarHeight() + TimeTable.LITTLE_VIEW_HEIGHT) && mTouchFlag == TOUCH_FLAG_EXTEND) {
                    mScrollBottomListener.onScrollBottom(true);
                    return false;
                }
                mWeekDayLayout.scrollBy((int) (mx - curX), 0);
                mScheduleLayout.scrollBy((int) (mx - curX), (int) (my - curY));
                mCourseLayout.scrollBy(0, (int) (my - curY));
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


    //设置是在那种情况下的滑动
    public void setTouchFlag(int touchFlag) {
        mTouchFlag = touchFlag;
    }

    //设置本周的日期
    public void setDate(int weekDistance) {

    }

    //添加每节课程的TextView
    public void setCourse(final List<Course> courses) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getId() != null && courses.get(i).getId().equals("0")) {

            } else {
                final int curCourse = i;
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        courses.get(i).getDuring() * COURSE_TIME_HEIGHT / 2 - 2
                );
                params.setMargins(0, COURSE_TIME_HEIGHT / 2 * (courses.get(i).getStart() - 1), 0, 0);
                TextView courseTv = new TextView(mContext);
                courseTv.setBackground(getResources().getDrawable(getRandomBg()));
                courseTv.setTextColor(Color.WHITE);
                String courseName = simplifyCourse(courses.get(i).getCourse());
                courseTv.setText(courseName + "\n@" +
                        courses.get(i).getPlace() + "\n" +
                        courses.get(i).getTeacher());

                courseTv.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mOnLongPressedListener.onLongPressed(courses.get(curCourse));
                        return true;
                    }
                });

                int j;
                j = transDaysToInt(courses.get(i).getDay());
                dayCourseLayout[j].addView(courseTv, params);
            }
        }
    }

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
        }
    }

    //获取随机的课程背景色
    public int getRandomBg() {
        Random random = new Random();
        int r = random.nextInt(4);
        if (r == 1) {
            return R.drawable.shape_rectangle_green;
        } else if (r == 2) {
            return R.drawable.shape_rectangle_orange;
        } else if (r == 3) {
            return R.drawable.shape_rectangle_pink;
        } else {
            return R.drawable.shape_rectangle_purple;
        }
    }


}
