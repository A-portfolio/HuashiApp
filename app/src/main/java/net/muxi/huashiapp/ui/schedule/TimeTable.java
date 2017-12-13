package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.RxBus;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.event.LoginSuccessEvent;
import net.muxi.huashiapp.event.RefreshFinishEvent;
import net.muxi.huashiapp.ui.login.LoginActivity;
import net.muxi.huashiapp.util.DimensUtil;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

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
    public static final int REFRESH_VIEW_HEIGHT = DimensUtil.dp2px(100);
    public static final int ABLE_TO_REFRESH_HEIGHT = DimensUtil.dp2px(82);
    public static final int REFRESHING_VIEW_HEIGHT = DimensUtil.dp2px(82);
    public static final int REFRESH_RESULT_VIEW_HEIGHT = DimensUtil.dp2px(48);

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
    AddNewCourseView mViewCurweekSet;
    @BindView(R.id.layout_table)
    TimeTableLayout mLayoutTable;
    @BindView(R.id.tv_tip)
    TextView mTvTip;

    private View curDownTargetView;
    private boolean isFirstShow = true;
    private Scroller mScroller;
    private Context mContext;
    private List<TextView> mCourseViews = new ArrayList<>();
    public static final boolean IS_WEEKDAY_SHOW = false;
    private float curX, curY;
    private float startX, startY;
    private long lastTime;
    private boolean isTouchCancel = true;
    private boolean isPulling = false;

    private OnClickListener mOnCourseClickListener;
    public TimeTable(Context context) {
        this(context, null);
    }
    public TimeTable(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mScroller = new Scroller(context);
        isFirstShow = PreferenceUtil.getBoolean(PreferenceUtil.IS_FIRST_ENTER_TABLE, true);
        initLayout();
        RxBus.getDefault().toObservable(RefreshFinishEvent.class)
                .subscribe(refreshFinishEvent -> {
                    if (refreshFinishEvent.isRefreshResult()) {
                        mRefreshView.setRefreshResult(R.string.tip_refresh_ok);
                        mRefreshView.setRefreshViewBackground(R.color.colorAccent);
                    } else{
                        mRefreshView.setRefreshResult(R.string.tip_refresh_retry);
                        mRefreshView.setRefreshViewBackground(R.color.red);
                        //说明token过期
                        if(refreshFinishEvent.getCode()==401){
                            String id =PreferenceUtil.getString(PreferenceUtil.STUDENT_ID);
                            String pwd = PreferenceUtil.getString(PreferenceUtil.STUDENT_PWD);
                            User user = new User();
                            user.setPassword(pwd);
                            user.setSid(id);

                            PreferenceUtil.clearString(PreferenceUtil.JSESSIONID);
                            PreferenceUtil.clearString(PreferenceUtil.BIG_SERVER_POOL);
                            LoginActivity loginActivity = new LoginActivity();
                            loginActivity.login(user)
                                    .subscribe(new Subscriber() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            e.printStackTrace();
                                        }

                                        @Override
                                        public void onNext(Object o) {
                                            boolean result = (boolean) o;
                                            if(result){
                                                RxBus.getDefault().send(LoginSuccessEvent.class);
                                            }
                                        }
                                    });
                        }
                    }
                    smoothScrollTo(0, -REFRESH_RESULT_VIEW_HEIGHT);
                    postDelayed(() -> {
                        mRefreshView.setReadyToPull();
                        smoothScrollTo(0, 0);
                    }, 1000);
                });
    }

    public void initLayout() {
        LayoutInflater.from(mContext).inflate(R.layout.view_timetable, this);
        ButterKnife.bind(this);
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

                if (!isFirstShow) {
                    if (mRefreshView.status == RefreshView.Status.REFRESH_FINISHED ||
                            mRefreshView.status == RefreshView.Status.REFRESHING) {
                        return true;
                    }
                }

                int scrolledX = mTimetableContent.getScrollX();
                int scrolledY = mTimetableContent.getScrollY();

                int xDistance = 0;
                int yDistance = 0;

                if (scrolledX + curX - event.getX() < 0) {
                    xDistance = -scrolledX;
                } else if (scrolledX + curX - event.getX()
                        > WEEK_DAY_WIDTH * 7 + LITTLE_VIEW_WIDTH - DimensUtil.getScreenWidth()) {
                    xDistance = WEEK_DAY_WIDTH * 7 + LITTLE_VIEW_WIDTH - DimensUtil.getScreenWidth()
                            - scrolledX;
                } else {
                    xDistance = (int) (curX - event.getX());
                }

                //不同情况下的区别滚动
                if (isFirstShow) {
                    if (scrolledY + curY - event.getY() < 0) {
                        yDistance = -scrolledY;
                    }
                } else {
                    if (getScrollY() + curY - event.getY() < 0 && scrolledY <= 1) {
                        isPulling = true;
                        if (getScrollY() + curY - event.getY() < 0
                                && getScrollY() + curY - event.getY() >= -REFRESH_VIEW_HEIGHT) {
                            yDistance = (int) (curY - event.getY()) / 2;
                            scrollBy(0, yDistance);
                            if (getScrollY() < -ABLE_TO_REFRESH_HEIGHT) {
                                mRefreshView.setReleaseToRefresh();
                            } else {
                                mRefreshView.setPullToRefresh();
                            }
                        } else if (getScrollY() + curY - event.getY() < -REFRESH_VIEW_HEIGHT) {
                            yDistance = -REFRESH_VIEW_HEIGHT - getScrollY();
                            scrollBy(0, yDistance);
                        }
                        curX = event.getX();
                        curY = event.getY();
                        return true;
                    }
                }

                //滚动课表
                if (scrolledY + curY - event.getY() >= 0 && scrolledY + curY - event.getY()
                        <= COURSE_TIME_HEIGHT * 14 + LITTLE_VIEW_HEIGHT + DimensUtil.dp2px(56) * 2
                        + DimensUtil.dp2px(60)
                        + DimensUtil.getStatusBarHeight() - DimensUtil.getScreenHeight()) {
                    yDistance = (int) (curY - event.getY());
                }

                if (scrolledY + curY - event.getY()
                        > COURSE_TIME_HEIGHT * 14 + LITTLE_VIEW_HEIGHT + DimensUtil.dp2px(56)
                        * 2
                        + DimensUtil.dp2px(60)
                        + DimensUtil.getStatusBarHeight() - DimensUtil.getScreenHeight()) {
                    yDistance = COURSE_TIME_HEIGHT * 14 + LITTLE_VIEW_HEIGHT + DimensUtil.dp2px(
                            56)
                            * 2 + DimensUtil.dp2px(56) + DimensUtil.getStatusBarHeight()
                            - DimensUtil.getScreenHeight()
                            - scrolledY;
                }

                mWeekLayout.scrollBy(xDistance, 0);
                mCourseTimeLayout.scrollBy(0, yDistance);
                mTimetableContent.scrollBy(xDistance, yDistance);

                curX = event.getX();
                curY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (curDownTargetView != null) {
                    if (System.currentTimeMillis() - lastTime < MAX_CLICK_DURATION
                            && distance(curX,
                            curY, startX, startY) < MAX_CLICK_DISTANCE) {
                        //如果是点击事件的话展示当前时间点的所有课程
                        if (mOnCourseClickListener != null) {
                            mOnCourseClickListener.onClick(curDownTargetView);
                        }
                        curDownTargetView = null;
                    }
                }
                Logger.d(mRefreshView.status.toString());
                if (mRefreshView.status == RefreshView.Status.PULL_TO_REFRESH) {
                    smoothScrollTo(0, 0);
                    mRefreshView.status = RefreshView.Status.READY_TO_PULL;
                } else if (mRefreshView.status == RefreshView.Status.RELEASE_TO_REFRESH) {
                    mRefreshView.setRefreshing();
                    smoothScrollTo(0, -REFRESHING_VIEW_HEIGHT);
//                    RxBus.getDefault().send(new RefreshTableEvent());
                    if (mOnRefreshListener != null){
                        mOnRefreshListener.onRefresh();
                    }
                    Logger.d("reload");
                }
                break;
        }

        return true;
    }

    public void smoothScrollTo(int x, int y) {
        Logger.d(getScrollX() + "y: " + getScrollY());
        mScroller.startScroll(getScrollX(), getScrollY(), x - getScrollX(), y - getScrollY());
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

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

    public void setOnCourseClickListener(OnClickListener listener) {
        mOnCourseClickListener = listener;
    }

    private OnRefreshListener mOnRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener){
        mOnRefreshListener = onRefreshListener;
    }

    public interface OnRefreshListener{
        void onRefresh();
    }

}
