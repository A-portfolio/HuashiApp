package net.muxi.huashiapp.schedule;

import android.animation.ObjectAnimator;
import android.content.Intent;
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

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.Base64Util;
import net.muxi.huashiapp.common.util.DimensUtil;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.common.util.PreferenceUtil;
import net.muxi.huashiapp.common.widget.TimeTable;

import butterknife.BindView;

import java.util.List;


import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/4/19.
 */
public class ScheduleActivity extends ToolbarActivity {

    // TODO: 16/6/21 添加课程时需要向服务端发 course


    public static int n = 0;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @BindView(R.id.schedule_hscrollview)
    WeekHScrollView mScheduleHscrollview;
    @BindView(R.id.schedule_ll)
    LinearLayout mScheduleLl;
    @BindView(R.id.tv_schedule_week_number)
    TextView mTvScheduleWeekNumber;
    @BindView(R.id.week_number_layout)
    RelativeLayout mWeekNumberLayout;
    @BindView(R.id.schedule_framelayout)
    FrameLayout mScheduleFramelayout;
    @BindView(R.id.root_layout)
    RelativeLayout mRootLayout;


    private PreferenceUtil sp;
    private HuaShiDao dao;

    //选择周数的 view 滑动时间
    private static final int DURATION_SLIDE = 200;

    private TimeTable mTimeTable;

    //当前用户所有的课程
    private List<Course> mCourses;
    private int mCurWeekNumber;

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

        dao = new HuaShiDao();
        sp = new PreferenceUtil();
        //获取当前周和当前用户的所有课程
        getCurWeek();
        getCurCourses();

        initView();
    }

    private void getCurWeek() {
        mCurWeekNumber = sp.getInt(PreferenceUtil.CUR_WEEK, 1);
    }

    private void getCurCourses() {
        mCourses = dao.loadCourse(new String("" + mCurWeekNumber));
        //如果当前未连接网络则不从服务器获取课表,使用本地课表
        if (!NetStatus.isConnected()){
            return;
        }
        User user = new User();
        user.setSid(sp.getString(PreferenceUtil.STUDENT_ID));
        user.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD));
        CampusFactory.getRetrofitService().getSchedule(Base64Util.createBaseStr(user),"2015","12","2014214629")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<List<Course>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Course> courses) {
                        //因为每次增删服务器与本地数据库都同时进行,所以就直接比较课程数有无差别
                        if (mCourses.size() != courses.size()) {
                            dao.deleteAllCourse();
                            for (int i = 0, max = courses.size(); i < max; i++) {
                                dao.insertCourse(courses.get(i));
                                mCourses.addAll(courses);
                            }
                        }
                        mTimeTable.setCourse(mCourses);
                    }
                });
    }

    private void initView() {
        mTimeTable = new TimeTable(this);

        LinearLayout.LayoutParams timeTableParams = new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTimeTable.setLayoutParams(timeTableParams);
        mScheduleLl.addView(mTimeTable);

        setTitle("课程表");
        // TODO: 16/5/25 debug
        mTvScheduleWeekNumber.setText(String.format(App.getContext().getResources().getString(R.string.course_week_format),
                sp.getInt(PreferenceUtil.CUR_WEEK, 1)));

        mScheduleHscrollview.setCurWeek(sp.getInt(PreferenceUtil.CUR_WEEK, 8));
        mScheduleHscrollview.setOnWeekChangeListener(new OnWeekChangeListener() {
            @Override
            public void OnWeekChange(int week) {
                //更新显示的当前周,更新课表
                mTvScheduleWeekNumber.setText(String.format(App.getContext().getResources().getString(R.string.course_week_format), week));
                mTimeTable.changeTheDate(week - sp.getInt(PreferenceUtil.CUR_WEEK, 8));
                mTimeTable.removeCourse();
                mTimeTable.setCourse(mCourses);
            }
        });

        mTimeTable.setCourse(mCourses);
        mTimeTable.setOnScrollBottomListener(new TimeTable.OnScrollBottomListener() {
            @Override
            public void onScrollBottom(boolean b) {
                if (b) {
                    beginBackAnim();
                }
            }
        });
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
//                startActivity(new Intent(ScheduleActivity.this,AddCourseActivity.class));
                Intent intent = new Intent(ScheduleActivity.this, AddCourseActivity.class);
                startActivityForResult(intent, 2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mTimeTable.setCourse(dao.loadCourse(getTheWeek(mTvScheduleWeekNumber.getText().toString())));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getTheWeek(String s) {
        int start = s.indexOf("第");
        int end = s.indexOf("周");
        return s.substring(start + 1, end);
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
