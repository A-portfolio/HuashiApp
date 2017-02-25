package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.util.DimensUtil;
import net.muxi.huashiapp.util.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/4/19.
 * 课程表显示的类
 */
public class TimeTable extends RelativeLayout {

    public static final int WEEK_DAY_WIDTH = DimensUtil.dp2px(65);
    public static final int COURSE_TIME_HEIGHT = DimensUtil.dp2px(57);
    public static final int LITTLE_VIEW_WIDTH = DimensUtil.dp2px(49);
    public static final int LITTLE_VIEW_HEIGHT = DimensUtil.dp2px(41);
    //course height = COURSE_TIME_HEIGHT * x -1-x
    public static final int COURSE_WIDTH = DimensUtil.dp2px(62);

    public static final int COURSE_TIME_DIVIDER = DimensUtil.dp2px(1);
    public static final int DIVIDER_WIDTH = DimensUtil.dp2px(1);

    public static final int MAX_CLICK_DISTANCE = DimensUtil.dp2px(15);
    public static final int MAX_CLICK_DURATION = 500;

    @BindView(R.id.timetable_content)
    TableContent mTimetableContent;
    @BindView(R.id.week_layout)
    WeekLayout mWeekLayout;
    @BindView(R.id.course_time_layout)
    CourseTimeLayout mCourseTimeLayout;
    @BindView(R.id.refresh_view)
    RefreshView mRefreshView;
    @BindView(R.id.view_curweek_set)
    CurweekSetView mViewCurweekSet;

    private ViewDragHelper mViewDragHelper;

    private View curDownTargetView;

    private Context mContext;
    private FrameLayout cornerView;
    private List<TextView> mCourseViews = new ArrayList<>();

    public static final boolean IS_WEEKDAY_SHOW = false;

    private float mx, my;
    private float curX, curY;
    private float startX, startY;
    private Date date1;

    private long lastTime;
    private boolean isTouchCancel = true;

    private OnClickListener mOnCourseClickListener;

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
        mViewDragHelper = ViewDragHelper.create(this,callback);
        Logger.d(getId() + "");
    }

    public void initLayout() {
        LayoutInflater.from(mContext).inflate(R.layout.view_timetable, this, true);
        ButterKnife.bind(this);
        setCornerView();
        setRefreshView();

//        mTimetableContent.setOnScrollChangeListener(new OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
//                TimeTable.super.onScrollChanged();
//            }
//        });
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

    public TableContent getTableContent() {
        return mTimetableContent;
    }

    public WeekLayout getWeekLayout() {
        return mWeekLayout;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();

                curX = event.getX();
                curY = event.getY();
                lastTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:

                int scrolledX = mTimetableContent.getScrollX();
                int scrolledY = mTimetableContent.getScrollY();

                int xDistance;
                int yDistance;

                if (scrolledX + curX - event.getX() < 0) {
                    xDistance = -scrolledX;
                } else if (scrolledX + curX - event.getX()
                        > WEEK_DAY_WIDTH * 7 + LITTLE_VIEW_WIDTH - DimensUtil.getScreenWidth()) {
                    xDistance = WEEK_DAY_WIDTH * 7 + LITTLE_VIEW_WIDTH - DimensUtil.getScreenWidth()
                            - scrolledX;
                } else {
                    xDistance = (int) (curX - event.getX());
                }

                if (scrolledY + curY - event.getY()
                        > COURSE_TIME_HEIGHT * 14 + LITTLE_VIEW_HEIGHT + DimensUtil.dp2px(56) * 3
                        + DimensUtil.getStatusBarHeight() - DimensUtil.getScreenHeight() ||
                        scrolledY + curY - event.getY() < 0) {
                    yDistance = -scrolledY;
                } else if (scrolledY + curY - event.getY()
                        > COURSE_TIME_HEIGHT * 14 + LITTLE_VIEW_HEIGHT + DimensUtil.dp2px(56) * 3
                        + DimensUtil.getStatusBarHeight() - DimensUtil.getScreenHeight()) {
                    yDistance = COURSE_TIME_HEIGHT * 14 + LITTLE_VIEW_HEIGHT + DimensUtil.dp2px(56)
                            * 3 + DimensUtil.getStatusBarHeight() - DimensUtil.getScreenHeight()
                            - scrolledY;
                } else {
                    yDistance = (int) (curY - event.getY());
                }

                mWeekLayout.scrollBy(xDistance, 0);
                mCourseTimeLayout.scrollBy(0, yDistance);
                mTimetableContent.scrollBy(xDistance, yDistance);

                curX = event.getX();
                curY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (curDownTargetView != null) {
                    if (System.currentTimeMillis() - lastTime < MAX_CLICK_DURATION && distance(curX,
                            curY, startX, startY) < MAX_CLICK_DISTANCE) {
                        //如果是点击事件的话展示当前时间点的所有课程
//                        showCurrentCourses(((CourseView) curDownTargetView).getCourseId());
                        if (mOnCourseClickListener != null){
                            mOnCourseClickListener.onClick(curDownTargetView);
                        }
                        curDownTargetView = null;
                    }
                }
                break;
        }
        return true;
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return false;
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return super.getViewVerticalDragRange(child);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return super.clampViewPositionVertical(child, top, dy);
        }
    };

    /**
     * 记录当前按下的 view
     */
    public void setCurDownTarget(View view) {
        curDownTargetView = view;
    }

    public float distance(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public void setOnCourseClickListener(OnClickListener listener){
        mOnCourseClickListener = listener;
    }

}
