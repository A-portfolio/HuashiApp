package net.muxi.huashiapp.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.AppConstants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.data.VerifyResponse;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.Base64Util;
import net.muxi.huashiapp.common.util.DateUtil;
import net.muxi.huashiapp.common.util.DimensUtil;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.common.util.PreferenceUtil;
import net.muxi.huashiapp.common.util.ToastUtil;
import net.muxi.huashiapp.common.widget.TimeTable;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/4/19.
 */
public class ScheduleActivity extends ToolbarActivity {


    public static int n = 0;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_schedule_week_number)
    TextView mTvScheduleWeekNumber;
    @BindView(R.id.img_pull)
    ImageView mImgPull;
    @BindView(R.id.week_number_layout)
    LinearLayout mWeekNumberLayout;
    @BindView(R.id.schedule_framelayout)
    FrameLayout mScheduleFramelayout;
    @BindView(R.id.root_layout)
    LinearLayout mRootLayout;

    private RecyclerView mRecyclerView;
    private WeekSelectAdapter adapter;

    private PreferenceUtil sp;
    private User mUser;
    private HuaShiDao dao;

    //选择周数的 view 滑动时间
    private static final int DURATION_SLIDE = 200;

    private TimeTable mTimeTable;

    //当前用户所有的课程
    private List<Course> mCourses;

    private int mCurWeek;

    //选中的周
    private int mSelectWeek;

    //标识当前处于是否选择周数显示的状态
    private boolean isSelectShown = false;
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
        mUser = new User();
        mUser.setSid(sp.getString(PreferenceUtil.STUDENT_ID));
        mUser.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD));
        //获取当前周和当前用户的所有课程
        getCurWeek();
        getCurCourses();

        initView();
    }

    private void getCurWeek() {
        int day = DateUtil.getDayInWeek(new Date(System.currentTimeMillis()));
        String defalutDate = DateUtil.getTheDateInYear(new Date(System.currentTimeMillis()),1 - day);
        mCurWeek = (int) DateUtil.getDistanceWeek(sp.getString(PreferenceUtil.FIRST_WEEK_DATE,defalutDate),DateUtil.toDateInYear(new Date(System.currentTimeMillis()))) + 1;
        mSelectWeek = mCurWeek;
    }

    private void getCurCourses() {
        mCourses = dao.loadCourse(new String("" + mSelectWeek));
        //如果当前未连接网络则不从服务器获取课表,使用本地课表
        if (!NetStatus.isConnected()) {
            return;
        }
        Logger.d(mUser.getPassword());
        Logger.d(mUser.getSid());
        CampusFactory.getRetrofitService().getSchedule(Base64Util.createBaseStr(mUser), "2015", "12", "2014214629")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<List<Course>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Course> courses) {
                        Logger.d(courses.size() + "");
                        //因为每次增删服务器与本地数据库都同时进行,所以就直接比较课程数有无差别
                        if (mCourses.size() != courses.size()) {
                            dao.deleteAllCourse();
                            for (int i = 0, max = courses.size(); i < max; i++) {
                                dao.insertCourse(courses.get(i));
                                mCourses.addAll(courses);
                            }
                        }
                        mCourses.addAll(courses);
                        mTimeTable.setCourse(mCourses);
                    }
                });
    }

    private void initView() {
        mTimeTable = new TimeTable(this);

        LinearLayout.LayoutParams timeTableParams = new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTimeTable.setLayoutParams(timeTableParams);
        mScheduleFramelayout.addView(mTimeTable);

        setupRecyclerview();
        mScheduleFramelayout.addView(mRecyclerView);

        setTitle("课程表");
        // TODO: 16/5/25 debug
        mTvScheduleWeekNumber.setText(String.format(App.getContext().getResources().getString(R.string.course_week_format),
                mSelectWeek));
        mTimeTable.setCourse(mCourses);
        mTimeTable.setOnLongPressedListener(new TimeTable.OnLongPressedListenr() {
            @Override
            public void onLongPressed(final Course course) {
                Logger.d(course.getId() + "");
                CampusFactory.getRetrofitService().deleteCourse(Base64Util.createBaseStr(mUser), String.valueOf(course.getId()))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Response<VerifyResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Response<VerifyResponse> verifyResponseResponse) {
                                if (verifyResponseResponse.code() == 200) {
                                    ToastUtil.showShort("delete success");
                                    dao.deleteCourse(course.getId());
                                    updateTimetable();
                                }

                            }
                        });

            }
        });
    }

    private void setupRecyclerview() {
        mRecyclerView = new RecyclerView(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        mRecyclerView.setBackgroundColor(getResources().getColor(android.R.color.white));
        mRecyclerView.setLayoutParams(layoutParams);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        adapter = new WeekSelectAdapter(mSelectWeek);
        adapter.setOnItemClickListener(new WeekSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mSelectWeek = position + 1;
                fadeoutRecyclerView();
                isSelectShown = false;
                invalidateOptionsMenu();
                mTvScheduleWeekNumber.setText(AppConstants.WEEKS[mSelectWeek - 1]);
                mTimeTable.changeTheDate(position + 1 - mCurWeek);

            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void updateTimetable() {
        mTimeTable.removeCourse();
        mTimeTable.setCourse(dao.loadCourse(getTheWeek(mTvScheduleWeekNumber.getText().toString())));
        Logger.d("schedule has update");
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
            case R.id.action_set_cur_week:
                final SetCurWeekView view = new SetCurWeekView(ScheduleActivity.this,mCurWeek);
                ViewGroup.LayoutParams contentParams = new ViewGroup.LayoutParams(
                        DimensUtil.dp2px(318),
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                view.setLayoutParams(contentParams);
                final MaterialDialog materialDialog = new MaterialDialog(ScheduleActivity.this);
                materialDialog.setTitle(getString(R.string.course_set_curweek_title))
                        .setContentView(view)
                        .setPositiveButton(R.string.btn_positive, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mSelectWeek = view.getSelectPostion();
                                materialDialog.dismiss();

                            }
                        })
                        .setNegativeButton(R.string.btn_negative, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                            }
                        });
                materialDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (isSelectShown){
            getMenuInflater().inflate(R.menu.menu_week_select,menu);
        }else {
            getMenuInflater().inflate(R.menu.menu_schedule,menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            updateTimetable();
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
        if (isSelectShown) {
            Logger.d("select");
            fadeoutRecyclerView();
            isSelectShown = false;
//            getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
            invalidateOptionsMenu();
        } else {
            Logger.d("select");
            fadeinRecyclerView();
            isSelectShown = true;
//            getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
            invalidateOptionsMenu();
        }
    }

    public void fadeinRecyclerView(){
        AlphaAnimation animation = new AlphaAnimation(0,1);
        animation.setDuration(200);
        animation.setFillBefore(true);
        animation.setFillAfter(false);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mRecyclerView.startAnimation(animation);
    }


    public void fadeoutRecyclerView(){
        AlphaAnimation animation = new AlphaAnimation(1,0);
        animation.setDuration(200);
        animation.setFillBefore(true);
        animation.setFillAfter(false);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mRecyclerView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mRecyclerView.startAnimation(animation);
    }

}
