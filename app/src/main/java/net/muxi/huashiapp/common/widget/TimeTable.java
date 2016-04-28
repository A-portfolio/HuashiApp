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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.util.DimensUtil;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.schedule.HScrollView;
import net.muxi.huashiapp.schedule.ScheduleTimeLayout;
import net.muxi.huashiapp.schedule.TimeTableLayout;
import net.muxi.huashiapp.schedule.VScrollView;

/**
 * Created by ybao on 16/4/19.
 */
public class TimeTable extends FrameLayout {

    public static final int FIXED_WIDTH = DimensUtil.getScreenWidth();
    public static final int WEEK_DAY_WIDTH = DimensUtil.dip2px(70);
    public static final int COURSE_TIME_HEIGHT = DimensUtil.dip2px(105);
    public static final int LITTLE_VIEW_WIDTH = DimensUtil.dip2px(40);
    public static final int LITTLE_VIEW_HEIGHT = DimensUtil.dip2px(40);
    public static final String TAG = "touch";


    private VScrollView mScrollView;
    private HScrollView mHorizontalScrollView;
    private TimeTableLayout mTableLayout;
    private TableRow[] mTableRows;
    //各课程的名称,上课地点和老师
    private TextView[][] mContentTextViews;

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

    public VScrollView getScrollView() {
        return mScrollView;
    }

    public HScrollView getHorizontalScrollView() {
        return mHorizontalScrollView;
    }

    public void setupCourseTimeLayout(Context context) {

        mCourseLayout = new ScheduleTimeLayout(context);
        LinearLayout.LayoutParams courseLayoutParams = new
                LinearLayout.LayoutParams(LITTLE_VIEW_WIDTH, COURSE_TIME_HEIGHT * 7 + LITTLE_VIEW_HEIGHT);
//        courseLayoutParams.setMargins(0, LITTLE_VIEW_HEIGHT, WEEK_DAY_WIDTH * 7, 0);
        mCourseLayout.setLayoutParams(courseLayoutParams);
        mCourseLayout.setOrientation(LinearLayout.VERTICAL);
        mCourseLayout.setPadding(0, LITTLE_VIEW_HEIGHT, 0, 0);
        mCourseLayout.setBackgroundColor(Color.WHITE);
        addView(mCourseLayout);

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
            mCourseTextView[i].setHeight(COURSE_TIME_HEIGHT / 2 );
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
//        LinearLayout.LayoutParams weekDayParams = new
//                LinearLayout.LayoutParams(WEEK_DAY_WIDTH * 7 + 4 + LITTLE_VIEW_WIDTH, LITTLE_VIEW_HEIGHT);
        LinearLayout.LayoutParams weekDayParams = new
                LinearLayout.LayoutParams(WEEK_DAY_WIDTH * 7, LITTLE_VIEW_HEIGHT);
//        weekDayParams.setMargins(LITTLE_VIEW_WIDTH,
//                0,
//                DimensUtil.getScreenWidth() - WEEK_DAY_WIDTH - LITTLE_VIEW_WIDTH,
//                COURSE_TIME_HEIGHT * 7);
        mWeekDayLayout.setLayoutParams(weekDayParams);
        mWeekDayLayout.setPadding(LITTLE_VIEW_WIDTH, 0, 0, 0);
        mWeekDayLayout.setOrientation(LinearLayout.HORIZONTAL);
        mWeekDayLayout.setBackgroundColor(Color.GREEN);
        addView(mWeekDayLayout);

        mWeekDayTextView = new TextView[7];
        ImageView[] divider = new ImageView[7];
        String[] weekdays = new String[7];
        weekdays = getResources().getStringArray(R.array.week_day);
//        List<String> weekdayLists = new ArrayList<>();
//        weekdayLists = DateUtil.getTheWeekDate();
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
            weekdays = getResources().getStringArray(R.array.week_day);
//            mWeekDayTextView[i].setText("feng");
//            weekdayLists[i] = DateUtil.getTheWeekDate().get(i);
//            mWeekDayTextView[i].setText(weekdays[i] + "\n" + weekdayLists.get(i));
            mWeekDayTextView[i].setText(weekdays[i]);
            mWeekDayLayout.addView(mWeekDayTextView[i]);
        }

    }


    public void setupScrollerView(Context context) {

//        View view = LayoutInflater.from(context).inflate(R.layout.view_time_table, null);
//        FrameLayout.LayoutParams viewParams = new FrameLayout.LayoutParams(WEEK_DAY_WIDTH * 7, COURSE_TIME_HEIGHT * 7);
//        viewParams.setMargins(LITTLE_VIEW_WIDTH, LITTLE_VIEW_HEIGHT, 0, 0);
//        view.setLayoutParams(viewParams);
//        mScrollView = (VScrollView) view.findViewById(R.id.schedule_scroll_view);
//        mHorizontalScrollView = (HScrollView) view.findViewById(R.id.schedule_hscroll_view);
        mTableLayout = new TimeTableLayout(context);
//        mTableLayout.setBackground(getResources().getDrawable(R.drawable.december));
//        addView(view);

//        setOnTouchEvent();

        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(WEEK_DAY_WIDTH * 7, COURSE_TIME_HEIGHT * 7 + LITTLE_VIEW_HEIGHT);
//        tableLayoutParams.setMargins(LITTLE_VIEW_WIDTH,LITTLE_VIEW_HEIGHT,0,0);
        mTableLayout.setLayoutParams(tableLayoutParams);
        mTableLayout.setPadding(LITTLE_VIEW_WIDTH, LITTLE_VIEW_HEIGHT, 0, 0);
        addView(mTableLayout);
        TableRow.LayoutParams tableRowParams = new
                TableRow.LayoutParams(WEEK_DAY_WIDTH * 7, COURSE_TIME_HEIGHT);
        mContentTextViews = new TextView[7][7];
        mTableRows = new TableRow[7];

        for (int i = 0; i < 7; i++) {
            mTableRows[i] = new TableRow(context);
            mTableRows[i].setBackgroundColor(Color.BLUE);
            mTableRows[i].setLayoutParams(tableRowParams);
            mTableLayout.addView(mTableRows[i]);

            for (int j = 0; j < 7; j++) {
                mContentTextViews[i][j] = new TextView(context);
                mContentTextViews[i][j].setBackgroundColor(Color.YELLOW);
                mContentTextViews[i][j].setWidth(WEEK_DAY_WIDTH);
                mContentTextViews[i][j].setGravity(Gravity.TOP);
                mContentTextViews[i][j].setHeight(COURSE_TIME_HEIGHT);
                mContentTextViews[i][j].setText("数据结构\n@7109\n 熊回香 ");
                mTableRows[i].addView(mContentTextViews[i][j]);
            }
        }

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
//                startX = mx;
//                startY = my;
                break;
            case MotionEvent.ACTION_MOVE:
                curX = event.getX();
                curY = event.getY();
//                dx = (int) (mx - curX);
//                dy = (int) (my - curY);
//                dx = checkPositionX(dx);
//                dy = checkPositionY(dy);

//                mScrollView.scrollBy((int) (mx - curX), (int) (my - curY));
//                mHorizontalScrollView.scrollBy((int) (mx - curX), (int) (my - curY));
                mCourseLayout.scrollBy(0, (int) (my - curY));
                mWeekDayLayout.scrollBy((int) (mx - curX), 0);
                mTableLayout.scrollBy((int) (mx - curX), (int) (my - curY));
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
                Logger.d(velocityX + "-----+" + velocityY);
//                mCourseLayout.smoothScrollBy(0, -velocityY / 2);
//                mWeekDayLayout.smoothScrollBy(-velocityX / 2, 0);
//                mTableLayout.smoothScrollBy(-velocityX / 2, -velocityY / 2);

                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
//                mScrollView.scrollBy((int) (mx - curX), (int) (my - curY));
//                mHorizontalScrollView.scrollBy((int) (mx - curX), (int) (my - curY));
//                mWeekDayLayout.smoothScrollBy((int) (startX - curX), 0);
//                mCourseLayout.smoothScrollBy(0, (int) (startY - curY));
//                mTableLayout.smoothScrollBy((int) (startX - curX), (int) (startY - curY));

                break;
        }

        return true;
    }

    private int checkPositionX(int dx) {
//        if ()
        return 1;
    }

    private int checkPositionY(int dy) {
        return 1;
    }

    //设置中间可滑动的课程表的触摸事件
    private void setOnTouchEvent() {
        mScrollView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float curX, curY;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mx = event.getX();
                        my = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        curX = event.getX();
                        curY = event.getY();

                        mHorizontalScrollView.scrollBy((int) (mx - curX), (int) (my - curY));
                        mScrollView.smoothScrollBy((int) (mx - curX), (int) (my - curY));

                        mx = curX;
                        my = curY;
                        break;
                    case MotionEvent.ACTION_UP:
                        curX = event.getX();
                        curY = event.getY();

                        mScrollView.scrollBy((int) (mx - curX), (int) (my - curY));
                        mHorizontalScrollView.scrollBy((int) (mx - curX), (int) (my - curY));
                        break;
                }
                return true;
            }
        });
    }
}
