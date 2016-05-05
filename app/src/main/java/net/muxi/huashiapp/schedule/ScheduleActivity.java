package net.muxi.huashiapp.schedule;

import android.animation.ObjectAnimator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.util.DimensUtil;
import net.muxi.huashiapp.common.widget.TimeTable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ybao on 16/4/19.
 */
public class ScheduleActivity extends ToolbarActivity {


    public static int n = 0;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @Bind(R.id.schedule_hscrollview)
    HorizontalScrollView mScheduleHscrollview;
    @Bind(R.id.schedule_ll)
    LinearLayout mScheduleLl;
    @Bind(R.id.tv_schedule_week_number)
    TextView mTvScheduleWeekNumber;
    @Bind(R.id.week_number_layout)
    RelativeLayout mWeekNumberLayout;
    @Bind(R.id.schedule_framelayout)
    FrameLayout mScheduleFramelayout;
    @Bind(R.id.root_layout)
    RelativeLayout mRootLayout;

    private TimeTable mTimeTable;
    private float mx, my;
    private boolean clickFlag = false;
    public static final int SELECT_WEEK_LAYOUT_HEIGHT = DimensUtil.dp2px(30);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);
        init();
        Logger.init();
    }

    private void init() {



        mTimeTable = new TimeTable(this);
        LinearLayout.LayoutParams timeTableParams = new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTimeTable.setLayoutParams(timeTableParams);
        mScheduleLl.addView(mTimeTable);


        initToolbar("课程表");
    }

    public int getActionbarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (this.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight =
                    TypedValue.complexToDimensionPixelSize(tv.data, this.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add_course:
                FragmentManager fm = getFragmentManager();
                fm.enableDebugLogging(true);
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.schedule_framelayout, new AddCourseFragment());
                ft.addToBackStack(null);

                if (n == 0) {
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                } else {
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                }
                n++;
                ft.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //选择查看第几周课程的点击事件
    @OnClick(R.id.week_number_layout)
    public void onClick() {
        if (clickFlag) {
            beginBackAnim();
            clickFlag = !clickFlag;
            mTimeTable.setTouchFlag(TimeTable.TOUCH_FLAG_BACK);
        } else {
            beginExtendAnim();
            clickFlag = !clickFlag;
            mTimeTable.setTouchFlag(TimeTable.TOUCH_FLAG_EXTEND);
        }
    }

    private void beginExtendAnim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mScheduleLl,
                "y",
                0,
                SELECT_WEEK_LAYOUT_HEIGHT);
        animator.setDuration(200);
        animator.start();
    }

    private void beginBackAnim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mScheduleLl,
                "y",
                SELECT_WEEK_LAYOUT_HEIGHT,
                0);
        animator.setDuration(200);
        animator.start();

    }
}
