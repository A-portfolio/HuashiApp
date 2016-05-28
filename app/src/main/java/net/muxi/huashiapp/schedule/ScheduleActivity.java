package net.muxi.huashiapp.schedule;

import android.animation.ObjectAnimator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.util.DimensUtil;
import net.muxi.huashiapp.common.util.PreferenceUtil;
import net.muxi.huashiapp.common.widget.TimeTable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ybao on 16/4/19.
 */
public class ScheduleActivity extends ToolbarActivity {


    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
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
    @Bind(R.id.schedule_hscrollview)
    WeekHScrollView mScheduleHscrollview;

    private PreferenceUtil sp;

    private String weekFormat = "第%d周";

    private HuaShiDao dao;

    //选择周数的 view 滑动时间
    private static final int DURATION_SLIDE = 200;

    private TimeTable mTimeTable;

    //标识当前处于是否选择周数显示的状态
    private boolean clickFlag = false;
    //选择周数layout 的高度
    public static final int SELECT_WEEK_LAYOUT_HEIGHT = DimensUtil.dp2px(40);
    //显示周数的 layout的高度
    public static final int WEEK_LAYOUT_HEIGHT = DimensUtil.dp2px(36);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);
        App.setCurrentActivity(this);
        dao = new HuaShiDao();
        sp = new PreferenceUtil();
        initView();
        Logger.init();
    }

    private void initView() {
        mTimeTable = new TimeTable(this);
        LinearLayout.LayoutParams timeTableParams = new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTimeTable.setLayoutParams(timeTableParams);
        mScheduleLl.addView(mTimeTable);

        initToolbar("课程表");
        // TODO: 16/5/25 debug
        mTvScheduleWeekNumber.setText(String.format(weekFormat, sp.getInt(PreferenceUtil.CUR_WEEK, 8)));
        mScheduleHscrollview.setCurWeek(sp.getInt(PreferenceUtil.CUR_WEEK,8));
        mScheduleHscrollview.setOnWeekChangeListener(new OnWeekChangeListener() {
            @Override
            public void OnWeekChange(int week) {
                //更新显示的当前周,更新课表
                mTvScheduleWeekNumber.setText(String.format(weekFormat, week));
                mTimeTable.changeTheDate(week - sp.getInt(PreferenceUtil.CUR_WEEK,8));
                mTimeTable.removeCourse();
                mTimeTable.setCourse(dao.loadCourse(mTvScheduleWeekNumber.getText().toString()));
            }
        });

        mTimeTable.setCourse(dao.loadCourse(mTvScheduleWeekNumber.getText().toString()));
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
                ft.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            setTitle("课程表");
            mTimeTable.setCourse(dao.loadCourse(mTvScheduleWeekNumber.getText().toString()));
        }
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
        } else {
            beginExtendAnim();
        }
    }

    public void beginExtendAnim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mScheduleLl,
                "y",
                0,
                WEEK_LAYOUT_HEIGHT);
        animator.setDuration(DURATION_SLIDE);
        animator.start();
        clickFlag = !clickFlag;
        mTimeTable.setTouchFlag(TimeTable.TOUCH_FLAG_EXTEND);
    }

    public void beginBackAnim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mScheduleLl,
                "y",
                WEEK_LAYOUT_HEIGHT,
                0);
        animator.setDuration(DURATION_SLIDE);
        animator.start();
        clickFlag = !clickFlag;
        mTimeTable.setTouchFlag(TimeTable.TOUCH_FLAG_BACK);

    }
}
