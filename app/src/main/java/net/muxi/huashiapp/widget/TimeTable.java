package net.muxi.huashiapp.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TimeUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.ui.schedule.CourseTimeLayout;
import net.muxi.huashiapp.ui.schedule.ScheduleTimeLayout;
import net.muxi.huashiapp.ui.schedule.TableContent;
import net.muxi.huashiapp.ui.schedule.WeekLayout;
import net.muxi.huashiapp.util.DateUtil;
import net.muxi.huashiapp.util.DimensUtil;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.PreferenceUtil;
import net.muxi.huashiapp.util.TimeTableUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/4/19.
 * 课程表显示的类
 */
public class TimeTable extends FrameLayout {

    public static final int WEEK_DAY_WIDTH = DimensUtil.dp2px(65);
    public static final int COURSE_TIME_HEIGHT = DimensUtil.dp2px(57);
    public static final int LITTLE_VIEW_WIDTH = DimensUtil.dp2px(49);
    public static final int LITTLE_VIEW_HEIGHT = DimensUtil.dp2px(41);
    //course height = COURSE_TIME_HEIGHT * x -1-x
    public static final int COURSE_WIDTH = DimensUtil.dp2px(62);

    public static final int COURSE_TIME_DIVIDER = DimensUtil.dp2px(1);
    public static final int DIVIDER_WIDTH = DimensUtil.dp2px(1);
    @BindView(R.id.timetable_content)
    TableContent mTimetableContent;
    @BindView(R.id.week_layout)
    WeekLayout mWeekLayout;
    @BindView(R.id.course_time_layout)
    CourseTimeLayout mCourseTimeLayout;


    private Context mContext;
    private FrameLayout cornerView;
    private List<TextView> mCourseViews = new ArrayList<>();

    public static final boolean IS_WEEKDAY_SHOW = false;

    private float mx, my;
    private float curX, curY;
    private float startX, startY;
    private Date date1;
    private boolean isTouchCancel = true;

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
        initLayout();
    }

    public void initLayout() {
        LayoutInflater.from(mContext).inflate(R.layout.view_timetable, this, true);
        ButterKnife.bind(this);
        setCornerView();
        setRefreshView();

    }

    private void setRefreshView() {

    }

    //绘制左上角的 view
    public void setCornerView() {
        cornerView = new FrameLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LITTLE_VIEW_WIDTH,
                LITTLE_VIEW_HEIGHT);
        cornerView.setLayoutParams(params);
        cornerView.setBackgroundColor(Color.WHITE);
        View dividerH = new View(mContext);
        View dividerV = new View(mContext);
        LayoutParams hParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, DIVIDER_WIDTH);
        LayoutParams vParams = new LayoutParams(DIVIDER_WIDTH,
                ViewGroup.LayoutParams.MATCH_PARENT);
        hParams.setMargins(0, DimensUtil.dp2px(40), 0, 0);
        vParams.setMargins(DimensUtil.dp2px(48), 0, 0, 0);
        dividerH.setBackgroundColor(getResources().getColor(R.color.divider));
        dividerV.setBackgroundColor(getResources().getColor(R.color.divider));
        cornerView.addView(dividerH, hParams);
        cornerView.addView(dividerV, vParams);
        addView(cornerView);
    }

    public TableContent getTableContent(){
        return mTimetableContent;
    }

    public WeekLayout getWeekLayout(){
        return mWeekLayout;
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
            case MotionEvent.ACTION_DOWN:
                curX = event.getX();
                curY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int scrolledX = mTimetableContent.getScrollX();
                int scrolledY = mTimetableContent.getScrollY();

                int xDistance;
                int yDistance;

                if (scrolledX + event.getX() - curX
                        > WEEK_DAY_WIDTH * 7 + LITTLE_VIEW_WIDTH - DimensUtil.getScreenWidth() ||
                        scrolledX + event.getX() - curX < 0) {
                    xDistance = (int)(curX - event.getX());
                } else {
                    xDistance = (int)(event.getX() - curX);
                }

                if (scrolledY + event.getY() - curY
                        > COURSE_TIME_HEIGHT * 14 + LITTLE_VIEW_HEIGHT + DimensUtil.dp2px(56) * 3
                        + DimensUtil.getStatusBarHeight() - DimensUtil.getScreenHeight() ||
                        scrolledY + event.getY() - curY < 0) {
                    yDistance = (int)(curY - event.getY());
                } else {
                    yDistance = (int)(event.getY() - curY);
                }

                mWeekLayout.scrollBy(xDistance,0);
                mCourseTimeLayout.scrollBy(0,yDistance);
                mTimetableContent.scrollBy(xDistance,yDistance);

                curX = event.getX();
                curY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }


}
