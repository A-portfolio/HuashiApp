package net.muxi.huashiapp.common.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.util.DateUtil;
import net.muxi.huashiapp.common.util.DimensUtil;
import net.muxi.huashiapp.schedule.ScheduleActivity;
import net.muxi.huashiapp.schedule.ScheduleTimeLayout;

import java.util.List;

/**
 * Created by ybao on 16/4/19.
 * 显示课表,星期,节数的类
 */
public class TimeTable extends FrameLayout {

    public static final int WEEK_DAY_WIDTH = DimensUtil.dp2px(70);
    public static final int COURSE_TIME_HEIGHT = DimensUtil.dp2px(105);
    public static final int LITTLE_VIEW_WIDTH = DimensUtil.dp2px(40);
    public static final int LITTLE_VIEW_HEIGHT = DimensUtil.dp2px(40);

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
    private TextView[] mCourseTextView;

    private ScheduleTimeLayout mWeekDayLayout;
    private TextView[] mWeekDayTextView;

    private float mx, my;
    private float curX, curY;

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
        view.setBackgroundColor(Color.BLUE);
        view.setLayoutParams(params);
        addView(view);
    }

    public void setupCourseTimeLayout(Context context) {

        mCourseLayout = new ScheduleTimeLayout(context);
        FrameLayout.LayoutParams courseLayoutParams = new
                FrameLayout.LayoutParams(LITTLE_VIEW_WIDTH, COURSE_TIME_HEIGHT * 7);
        courseLayoutParams.setMargins(0, LITTLE_VIEW_HEIGHT, 0, 0);
        mCourseLayout.setOrientation(LinearLayout.VERTICAL);
        mCourseLayout.setBackgroundColor(Color.WHITE);
        addView(mCourseLayout, courseLayoutParams);

        mCourseTextView = new TextView[14];

        for (int i = 0; i < 14; i++) {
            //添加分割线
            ViewGroup.LayoutParams dividerParams = new
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            View divider = new View(context);
            divider.setBackgroundColor(Color.BLACK);
            divider.setLayoutParams(dividerParams);
            mCourseLayout.addView(divider);

            mCourseTextView[i] = new TextView(context);
            mCourseTextView[i].setGravity(Gravity.CENTER);
            mCourseTextView[i].setWidth(LITTLE_VIEW_WIDTH);
            mCourseTextView[i].setHeight(COURSE_TIME_HEIGHT / 2 - 1);
            mCourseTextView[i].setBackgroundColor(Color.RED);
            String hour = "" + (i / 2 * 2 + 8);
            String minute;
            if (i % 2 == 0) {
                minute = "00";
            } else minute = "" + 55;
            mCourseTextView[i].setText(hour + ":" + minute + "\n" + (i + 1));
            mCourseLayout.addView(mCourseTextView[i]);

        }
    }


    public void setupWeekDayLayout(Context context) {

        mWeekDayLayout = new ScheduleTimeLayout(context);
        FrameLayout.LayoutParams weekDayParams = new
                FrameLayout.LayoutParams(WEEK_DAY_WIDTH * 7, LITTLE_VIEW_HEIGHT);
        mWeekDayLayout.setLayoutParams(weekDayParams);
        mWeekDayLayout.setPadding(LITTLE_VIEW_WIDTH, 0, 0, 0);
        mWeekDayLayout.setOrientation(LinearLayout.HORIZONTAL);
        mWeekDayLayout.setBackgroundColor(Color.GREEN);
        addView(mWeekDayLayout, weekDayParams);

        mWeekDayTextView = new TextView[7];

        ImageView[] divider = new ImageView[7];
        weekDates = DateUtil.getTheWeekDate(0);
        weekdays = getResources().getStringArray(R.array.week_day);
        for (int i = 0; i < 7; i++) {

            ViewGroup.LayoutParams dividerParams = new ViewGroup.LayoutParams(
                    1,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            divider[i] = new ImageView(context);
            divider[i].setBackgroundColor(Color.BLACK);
            divider[i].setLayoutParams(dividerParams);
            mWeekDayLayout.addView(divider[i]);

            mWeekDayTextView[i] = new TextView(context);
            mWeekDayTextView[i].setLayoutParams(new
                    ViewGroup.LayoutParams(WEEK_DAY_WIDTH - 1, ViewGroup.LayoutParams.MATCH_PARENT));
            mWeekDayTextView[i].setGravity(Gravity.CENTER);
            mWeekDayTextView[i].setText(weekdays[i] + "\n" + weekDates.get(i));
            mWeekDayLayout.addView(mWeekDayTextView[i]);
        }
    }


    //更改对应周的日期,传入的distance为选择周距当前周的周数
    public void changeTheDate(int distance) {
        weekDates = DateUtil.getTheWeekDate(distance);
        for (int i = 0; i < 7; i++) {
            mWeekDayTextView[i].setText(weekdays[i] + "\n" + weekDates.get(i));
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

        for (int i = 0; i < 7; i++) {

            LinearLayout.LayoutParams relativeParams = new LinearLayout.LayoutParams(
                    WEEK_DAY_WIDTH,
                    COURSE_TIME_HEIGHT * 7
            );
            dayCourseLayout[i] = new FrameLayout(context);
            dayCourseLayout[i].setBackgroundColor(Color.YELLOW);
            dayCourseLayout[i].setLayoutParams(relativeParams);
            mScheduleLayout.addView(dayCourseLayout[i]);

        }

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
                    ((ScheduleActivity) App.getCurrentActivity()).beginBackAnim();
                    return false;
                }
                mWeekDayLayout.scrollBy((int) (mx - curX), 0, mTouchFlag);
                mScheduleLayout.scrollBy((int) (mx - curX), (int) (my - curY), mTouchFlag);
                mCourseLayout.scrollBy(0, (int) (my - curY), mTouchFlag);
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
    public void setCourse(List<Course> courses) {
        for (int i = 0; i < courses.size(); i++) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    courses.get(i).getDuration() * COURSE_TIME_HEIGHT / 2
            );
            params.setMargins(0, COURSE_TIME_HEIGHT / 2 * courses.get(i).getTime(), 0, 0);
            TextView courseTv = new TextView(mContext);
            courseTv.setBackgroundColor(Color.BLUE);
            courseTv.setText(courses.get(i).getCourseName() + "\n" +
                    courses.get(i).getTeacher() + "\n" +
                    courses.get(i).getPlace());

            int j = 0;
            switch (courses.get(i).getWeeks()) {
                case "星期一":
                    j = 0;
                    break;
                case "星期二":
                    j = 1;
                    break;
                case "星期三":
                    j = 2;
                    break;
                case "星期四":
                    j = 3;
                    break;
                case "星期五":
                    j = 4;
                    break;
                case "星期六":
                    j = 5;
                    break;
                case "星期天":
                    j = 6;
                    break;
            }
            dayCourseLayout[j].addView(courseTv, params);
        }
    }


    //删除所有课,在更新课程表视图时开始调用
    public void removeCourse() {
        for (int i = 0; i < 7; i++) {
            if (dayCourseLayout[i].getChildCount() > 0) {
                dayCourseLayout[i].removeAllViews();
            }
        }
    }


}
