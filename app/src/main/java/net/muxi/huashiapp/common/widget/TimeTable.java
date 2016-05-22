package net.muxi.huashiapp.common.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.util.DateUtil;
import net.muxi.huashiapp.common.util.DimensUtil;
import net.muxi.huashiapp.schedule.ScheduleTimeLayout;

import java.util.List;

/**
 * Created by ybao on 16/4/19.
 */
public class TimeTable extends FrameLayout {

    public static final int FIXED_WIDTH = DimensUtil.getScreenWidth();
    public static final int WEEK_DAY_WIDTH = DimensUtil.dp2px(70);
    public static final int COURSE_TIME_HEIGHT = DimensUtil.dp2px(105);
    public static final int LITTLE_VIEW_WIDTH = DimensUtil.dp2px(40);
    public static final int LITTLE_VIEW_HEIGHT = DimensUtil.dp2px(40);

    public static final int TOUCH_FLAG_EXTEND = 2;
    public static final int TOUCH_FLAG_BACK = 1;
    private int mTouchFlag = 1;

    public static final String TAG = "touch";
    public static boolean sIsTouchable = true;

    public static final int MIN_SPEED = 200;

    private List<String> weekDate;

    private ScheduleTimeLayout mScheduleLayout;
    private RelativeLayout[] mRelativeLayout;


    List<String> weekDates;

    private View view;
    private ScheduleTimeLayout mCourseLayout;
    private TextView[] mCourseTextView;

    private ScheduleTimeLayout mWeekDayLayout;
    private TextView[] mWeekDayTextView;

    private float mx, my;
    private float curX, curY;
    private int dx = 0;
    private int dy = 0;
    private VelocityTracker mVelocityTracker;

    public TimeTable(Context context) {
        this(context, null);
    }

    public TimeTable(Context context, AttributeSet attrs) {
        super(context, attrs);

        initLayout(context);
    }

    public void initLayout(Context context) {
        Log.d("ss", App.getContext().toString());
        setupScrollerView(context);
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
//        courseLayoutParams.setMargins(0, LITTLE_VIEW_HEIGHT, WEEK_DAY_WIDTH * 7, 0);
        courseLayoutParams.setMargins(0,LITTLE_VIEW_HEIGHT,0,0);
        mCourseLayout.setOrientation(LinearLayout.VERTICAL);
        mCourseLayout.setBackgroundColor(Color.WHITE);
        addView(mCourseLayout,courseLayoutParams);

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
            mCourseTextView[i].setHeight(COURSE_TIME_HEIGHT / 2  - 1);
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
        addView(mWeekDayLayout,weekDayParams);

        mWeekDayTextView = new TextView[7];

//        Observable.just(0)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.computation())
//                .subscribe(new Observer<Integer>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        weekDate = DateUtil.getTheWeekDate(integer);
//                    }
//                });

        ImageView[] divider = new ImageView[7];
        weekDates = DateUtil.getTheWeekDate(0);
        for(int i=0;i<weekDates.size();i++){
            Log.d("what",weekDates.get(i));
        }
        String[] weekdays;
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
            mWeekDayTextView[i].setText(weekdays[i] + "\n" + weekDates.get(i)  );
            mWeekDayLayout.addView(mWeekDayTextView[i]);
        }

    }


    public void setupScrollerView(Context context) {

        mScheduleLayout = new ScheduleTimeLayout(context);


        FrameLayout.LayoutParams scheduleLayoutParams = new
                FrameLayout.LayoutParams(WEEK_DAY_WIDTH * 7, COURSE_TIME_HEIGHT * 7 );
        scheduleLayoutParams.setMargins(LITTLE_VIEW_WIDTH,LITTLE_VIEW_HEIGHT,0,0);
        mScheduleLayout.setLayoutParams(scheduleLayoutParams);

        addView(mScheduleLayout,scheduleLayoutParams);

        mRelativeLayout = new RelativeLayout[7];

        for (int i = 0; i < 7; i++) {

            LinearLayout.LayoutParams relativeParams = new LinearLayout.LayoutParams(
                    WEEK_DAY_WIDTH,
                    COURSE_TIME_HEIGHT * 7
            );
            mRelativeLayout[i] = new RelativeLayout(context);
            mRelativeLayout[i].setBackgroundColor(Color.YELLOW);
            mRelativeLayout[i].setLayoutParams(relativeParams);
            mScheduleLayout.addView(mRelativeLayout[i]);

//            for (int j = 0; j < 7; j++) {
//                mContentTextViews[i][j] = new TextView(context);
//                mContentTextViews[i][j].setBackgroundColor(Color.YELLOW);
//                mContentTextViews[i][j].setWidth(WEEK_DAY_WIDTH);
//                mContentTextViews[i][j].setGravity(Gravity.TOP);
//                mContentTextViews[i][j].setHeight(COURSE_TIME_HEIGHT);
//                mContentTextViews[i][j].setText("数据结构\n@7109\n 熊回香 ");
//                mTableRows[i].addView(mContentTextViews[i][j]);
//            }
        }

        mRelativeLayout[4].setBackgroundColor(Color.RED);
        mRelativeLayout[3].setBackgroundColor(Color.alpha(Color.WHITE));

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int dx, dy;
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mx = event.getX();
                my = event.getY();
//                if (!mWeekDayLayout.mScroller.isFinished()){
//                    mWeekDayLayout.mScroller.abortAnimation();
//                    mCourseLayout.mScroller.abortAnimation();
//                    mTableLayout.mScroller.abortAnimation();
//                }
                break;
            case MotionEvent.ACTION_MOVE:
                curX = event.getX();
                curY = event.getY();
                mWeekDayLayout.scrollBy((int) (mx - curX), 0,mTouchFlag);
                mScheduleLayout.scrollBy((int) (mx - curX), (int) (my - curY),mTouchFlag);
                mCourseLayout.scrollBy(0, (int) (my - curY),mTouchFlag);
                mx = curX;
                my = curY;
                break;
            case MotionEvent.ACTION_UP:
                curX = event.getX();
                curY = event.getY();
//                mVelocityTracker.computeCurrentVelocity(1000);
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int velocityX = (int) velocityTracker.getXVelocity();
                int velocityY = (int) velocityTracker.getYVelocity();
                if (isLowerMinSpeed(velocityX)){
                    velocityX = 0;
                }
                if (isLowerMinSpeed(velocityY)){
                    velocityY = 0;
                }
//                mCourseLayout.smoothScrollBy(0, 500,1);
//                mWeekDayLayout.smoothScrollBy(-velocityX / 2, 0);
//                mTableLayout.smoothScrollBy(-velocityX / 2, -velocityY / 2);

                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }

                break;
        }

        return true;
    }

    public boolean isLowerMinSpeed(int speed){
        if (speed < MIN_SPEED){
            return true;
        }
        return false;
    }

    public void setTouchFlag(int touchFlag){
        mTouchFlag = touchFlag;
    }

    //设置本周的日期
    public void setDate(int weekDistance){

    }

    //设置课程的具体内容
    public void setCourse(int weekday,int time,String info){

    }

}
