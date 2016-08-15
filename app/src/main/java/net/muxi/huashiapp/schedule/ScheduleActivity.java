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

import com.muxi.material_dialog.MaterialDialog;

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
import net.muxi.huashiapp.common.util.TimeTableUtil;
import net.muxi.huashiapp.common.util.ToastUtil;
import net.muxi.huashiapp.common.widget.ShadowView;
import net.muxi.huashiapp.common.widget.TimeTable;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/4/19.
 */
public class ScheduleActivity extends ToolbarActivity {

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
    FrameLayout mRootLayout;

    private RecyclerView mRecyclerView;
    private WeekSelectAdapter mWeekSelectAdapter;

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
        String defalutDate = DateUtil.getTheDateInYear(new Date(System.currentTimeMillis()), 1 - day);
        mCurWeek = (int) DateUtil.getDistanceWeek(sp.getString(PreferenceUtil.FIRST_WEEK_DATE, defalutDate), DateUtil.toDateInYear(new Date(System.currentTimeMillis()))) + 1;
        mSelectWeek = mCurWeek;
    }

    /**
     * 存储第一周的第一天日期
     */
    private void saveFirstWeekDate() {
        Date today = new Date(System.currentTimeMillis());
        String date = DateUtil.getTheDateInYear(today, (1 - mCurWeek) * 7 - DateUtil.getDayInWeek(today) + 1);
        sp.saveString(PreferenceUtil.FIRST_WEEK_DATE, date);
    }

    private void getCurCourses() {
        mCourses = dao.loadAllCourses();
        //如果当前未连接网络则不从服务器获取课表,使用本地课表
        if (!NetStatus.isConnected()) {
            return;
        }
        CampusFactory.getRetrofitService().getSchedule(Base64Util.createBaseStr(mUser), "2015", "12", App.sUser.getSid())
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
                            mCourses.clear();
                            for (int i = 0, max = courses.size(); i < max; i++) {
                                dao.insertCourse(courses.get(i));
                            }
                            mCourses.addAll(courses);
                            updateTimetable();
                        }

                    }
                });
    }

    private void initView() {
        mTimeTable = new TimeTable(this);
        mTimeTable.setTodayLayout(DateUtil.getDayInWeek(new Date(System.currentTimeMillis())));
        LinearLayout.LayoutParams timeTableParams = new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTimeTable.setLayoutParams(timeTableParams);
        mScheduleFramelayout.addView(mTimeTable);

        setupRecyclerview();
        mScheduleFramelayout.addView(mRecyclerView);

        setTitle("课程表");
        // TODO: 16/5/25 debug
        mTvScheduleWeekNumber.setText(AppConstants.WEEKS[mSelectWeek - 1]);
        mTimeTable.setCourse(mCourses, mSelectWeek);
        Logger.d("timetable set course" + mCourses.size());
        mTimeTable.setOnLongPressedListener(new TimeTable.OnLongPressedListenr() {
            @Override
            public void onLongPressed(final Course course) {
                Logger.d(course.getId() + "");
                final MaterialDialog dialog = new MaterialDialog(ScheduleActivity.this);
                dialog.setTitle(getResources().getString(R.string.course_delete_course_title))
                        .setButtonColor(getResources().getColor(R.color.colorPrimary))
                        .setTitleColor(getResources().getColor(R.color.course_dialog_light_title_color))
                        .setTitleSize(16)
                        .setNegativeButton(getResources().getString(R.string.btn_negative), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.btn_positive), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

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
                                                    dao.deleteCourse(course.getId());
                                                    mCourses = dao.loadAllCourses();
                                                    ToastUtil.showShort("删除成功");
                                                    updateTimetable();
                                                }

                                            }
                                        });

                                dialog.dismiss();
                            }
                        });

                dialog.show();

            }
        });

        mTimeTable.setOnCourseClickListener(new TimeTable.OnCourseClickListener() {
            @Override
            public void onCourseClick(Course course) {
                List<Course> displayCourse = TimeTableUtil.getAllCoursesInPosition(course, mCourses);
                CoursesView coursesView = new CoursesView(ScheduleActivity.this, displayCourse, mSelectWeek);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                ShadowView shadowView = new ShadowView(ScheduleActivity.this);
                shadowView.setLayoutParams(layoutParams);
                shadowView.setTag("shadow_view");
                coursesView.setTag("course_view");
                mRootLayout.addView(shadowView);
                mRootLayout.addView(coursesView, layoutParams);
                coursesView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
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
        mWeekSelectAdapter = new WeekSelectAdapter(mSelectWeek);
        mWeekSelectAdapter.setOnItemClickListener(new WeekSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mSelectWeek = position + 1;
                fadeoutRecyclerView();
                isSelectShown = false;
                invalidateOptionsMenu();
                mTvScheduleWeekNumber.setText(AppConstants.WEEKS[mSelectWeek - 1]);
                mTimeTable.changeTheDate(position + 1 - mCurWeek);
                if (mSelectWeek == mCurWeek) {
                    mTimeTable.setTodayLayout(DateUtil.getDayInWeek(new Date(System.currentTimeMillis())));
                    mTimeTable.setType(0);
                } else {
                    mTimeTable.resetTodayLayout(DateUtil.getDayInWeek(new Date(System.currentTimeMillis())));
                    mTimeTable.setType(1);
                    mTimeTable.scrollScheduleLayout(0, 0);
                }
                updateTimetable();
                mTimeTable.invalidate();

            }
        });
        mRecyclerView.setAdapter(mWeekSelectAdapter);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void updateTimetable() {
        mTimeTable.removeCourse();
        mTimeTable.setCourse(mCourses, mSelectWeek);
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
                CurweekSetDialog dialog = new CurweekSetDialog(ScheduleActivity.this, mCurWeek);
                dialog.setOnDialogPostiveClickListener(new CurweekSetDialog.OnDialogPostiveClickListener() {
                    @Override
                    public void onDialogPostiveClick(int curWeek) {
                        mCurWeek = curWeek;
                        saveFirstWeekDate();
                        mWeekSelectAdapter.swap(curWeek);
                        mSelectWeek = curWeek;
                        mTvScheduleWeekNumber.setText(AppConstants.WEEKS[mSelectWeek - 1]);
                    }
                });
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (isSelectShown) {
            getMenuInflater().inflate(R.menu.menu_week_select, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_schedule, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mCourses = dao.loadAllCourses();
            updateTimetable();
            mTimeTable.invalidate();
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
        if (isSelectShown) {
            fadeoutRecyclerView();
            isSelectShown = false;
            invalidateOptionsMenu();
            return;
        }
        if (isCourseViewShown()) {
            removeCourseView();
            return;
        }
        super.onBackPressed();
    }

    public boolean isCourseViewShown() {
        if (mRootLayout.findViewWithTag("shadow_view") != null) {
            return true;
        } else {
            return false;
        }
    }

    //移除展示课程以及阴影部分的 view
    public void removeCourseView() {
        mRootLayout.removeView(mRootLayout.findViewWithTag("shadow_view"));
        mRootLayout.removeView(mRootLayout.findViewWithTag("course_view"));
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
            mImgPull.setImageResource(R.drawable.arrow_drop_down);
        } else {
            Logger.d("select");
            fadeinRecyclerView();
            isSelectShown = true;
//            getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
            invalidateOptionsMenu();
            mImgPull.setImageResource(R.drawable.arrow_drop_up);
        }
    }

    public void fadeinRecyclerView() {
        AlphaAnimation animation = new AlphaAnimation(0, 1);
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


    public void fadeoutRecyclerView() {
        AlphaAnimation animation = new AlphaAnimation(1, 0);
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
