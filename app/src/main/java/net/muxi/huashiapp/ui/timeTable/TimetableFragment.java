package net.muxi.huashiapp.ui.timeTable;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.RxBus;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.base.BaseFragment;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.event.AddCourseEvent;
import net.muxi.huashiapp.event.AuditCourseEvent;
import net.muxi.huashiapp.event.CurWeekChangeEvent;
import net.muxi.huashiapp.event.DeleteCourseOkEvent;
import net.muxi.huashiapp.event.RefreshFinishEvent;
import net.muxi.huashiapp.event.RefreshTableEvent;
import net.muxi.huashiapp.net.CampusFactory;
import net.muxi.huashiapp.provider.ScheduleWidgetProvider;
import net.muxi.huashiapp.ui.login.LoginPresenter;
import net.muxi.huashiapp.util.AppStaticUtils;
import net.muxi.huashiapp.util.DimensUtil;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.PreferenceUtil;
import net.muxi.huashiapp.util.TimeTableUtil;
import net.muxi.huashiapp.util.TipViewUtil;
import net.muxi.huashiapp.widget.IndicatedView.IndicatedView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.muxi.huashiapp.widget.IndicatedView.IndicatedView.DIRECTION_DOWN;

/**
 * Created by ybao on 17/1/25.
 */

public class TimetableFragment extends BaseFragment {
    @BindView(R.id.timetable)
    TimeTable mTimetable;
    @BindView(R.id.week_selected_view)
    WeekSelectedView mWeekSelectedView;
    @BindView(R.id.tv_select_week)
    TextView mTvSelectWeek;
    @BindView(R.id.iv_select_week)
    ImageView mIvSelectWeek;
    @BindView(R.id.tv_current_week)
    TextView mTvCurrentWeek;
    @BindView(R.id.iv_menu)
    ImageButton mIvMenu;
    @BindView(R.id.tool_layout)
    RelativeLayout mToolLayout;
    @BindView(R.id.table_menu_view)
    TableMenuView mTableMenuView;
    @BindView(R.id.shade_view)
    View mShadeView;

    /**
     * 本学期所有的课程
     */
    private List<Course> mCourses;
    //false表示初始状态
    private boolean selectedIvStatus = false;
    private int selectedWeek;
    private int curWeek;
    private HuaShiDao dao;
    private Context mContext;
    private boolean handlingRefresh = false;
    public static TimetableFragment newInstance() {
        Bundle args = new Bundle();
        TimetableFragment fragment = new TimetableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        ButterKnife.bind(this, view);
        getActivity().getWindow().getDecorView().setBackgroundColor
                (Color.argb(255, 250, 250, 250));
        dao = new HuaShiDao();
        mContext = getActivity();
        initData();
        initView();
        initListener();

        return view;
    }

    private void initData() {
        curWeek = TimeTableUtil.getCurWeek();
        selectedWeek = curWeek;
        mCourses = dao.loadAllCourses();
    }
    private void initView() {
        if (mCourses.size() == 0) {
        } else {
            renderCourseView(mCourses);
        }
        setCurweek(TimeTableUtil.getCurWeek());
        setSelectedWeek(TimeTableUtil.getCurWeek());
        if (PreferenceUtil.getBoolean(PreferenceUtil.IS_FIRST_ENTER_TABLE, true)) {
            IndicatedView indicatedView = new IndicatedView(getContext());
            indicatedView.setTipViewText("设置当前周也可以点这里噢");
            TipViewUtil.addToContent(getContext(), indicatedView, DIRECTION_DOWN,
                    DimensUtil.getScreenWidth() - DimensUtil.dp2px(38), DimensUtil.dp2px(38));
        }
    }
    private void initListener() {
        mWeekSelectedView.setOnWeekSelectedListener(week -> {
            rotateSelectView();
            selectedWeek = week;
            renderCourseView(mCourses);
            setSelectedWeek(week);
            mShadeView.setVisibility(View.GONE);
        });
        mIvMenu.setOnClickListener(v -> {
            if (selectedIvStatus) {
                mWeekSelectedView.slideUp();
                rotateSelectView();
            }
            mTableMenuView.show();
            mTableMenuView.setCurweek(curWeek);
        });
        mTimetable.setOnCourseClickListener(v -> {
            String id = ((CourseView) v).getCourseId();
            DetailCoursesDialog dialog = DetailCoursesDialog.newInstance(getSameTimeCourses(id),
                    selectedWeek);
            dialog.show(getChildFragmentManager(), "detail_courses");
        });
        mShadeView.setOnClickListener(v -> {
            mTvSelectWeek.performClick();
            if (selectedIvStatus) {
                mWeekSelectedView.slideUp();
                mShadeView.setVisibility(View.GONE);
                rotateSelectView();
            }

        });


        Subscription subscription1
                = RxBus.getDefault().toObservable(AddCourseEvent.class)
                .subscribe(addCourseEvent -> {
                    mCourses = dao.loadAllCourses();
                    renderCourseView(mCourses);
                    Intent intent = new Intent(mContext, ScheduleWidgetProvider.class);
                    intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                    mContext.sendBroadcast(intent);
                }, Throwable::printStackTrace);
        ((BaseActivity) getContext()).addSubscription(subscription1);
        Subscription subscription2 = RxBus.getDefault().toObservable(DeleteCourseOkEvent.class)
                .subscribe(deleteCourseOkEvent -> {
                    mCourses = dao.loadAllCourses();
                    renderCourseView(mCourses);
                    Intent intent = new Intent(mContext, ScheduleWidgetProvider.class);
                    intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                    mContext.sendBroadcast(intent);
                }, Throwable::printStackTrace);
        ((BaseActivity) getContext()).addSubscription(subscription2);
        Subscription subscription3 = RxBus.getDefault().toObservable(RefreshTableEvent.class)
                .subscribe(refreshTableEvent -> {
                    Logger.d("refresh course");
                    mCourses = dao.loadAllCourses();
                    renderCourseView(mCourses);
                    Intent intent = new Intent(mContext, ScheduleWidgetProvider.class);
                    intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                    mContext.sendBroadcast(intent);
                }, Throwable::printStackTrace);
        ((BaseActivity) getContext()).addSubscription(subscription3);
        Subscription subscription4 = RxBus.getDefault().toObservable(CurWeekChangeEvent.class)
                .subscribe(curWeekChangeEvent -> {
                    setCurweek(TimeTableUtil.getCurWeek());
                    setSelectedWeek(TimeTableUtil.getCurWeek());
                    renderCourseView(mCourses);
                }, Throwable::printStackTrace);
        ((BaseActivity) getContext()).addSubscription(subscription4);
        RxBus.getDefault().toObservable(AuditCourseEvent.class)
        .subscribe(auditCourseEvent -> {loadTable();});

        mTimetable.setOnRefreshListener(() -> {
            handlingRefresh = true;
            loadTable();
        });
    }
    public List<Course> getSameTimeCourses(String id) {
        List<Course> courseList = new ArrayList<>();
        int start = 1;
        int duration = 2;
        String weekday = "星期一";
        for (Course course : mCourses) {
            if (course.id.equals(id)) {
                start = course.start;
                duration = course.during;
                    weekday = course.day;
                break;
            }
        }
        for (Course course : mCourses) {
            if (course.start == start && course.during == duration && course.day.equals(weekday)) {
                courseList.add(course);
            }
        }
        return courseList;
    }

    public void retryLoadTable(){
        new LoginPresenter()
                .login(App.sUser)
                .flatMap(booleans->CampusFactory.getRetrofitService().
                getTimeTable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(courses -> {
                    updateDB(courses);
                    mCourses = courses;
                    renderCourseView(courses);
                    if(handlingRefresh){
                        handlingRefresh = false;
                        RxBus.getDefault().send(new RefreshFinishEvent(courses.size()!=0));
                    }
                });
    }
    public void loadTable() {
           getTable();
    }

    private void getTable(){
        CampusFactory.getRetrofitService().getTimeTable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(courseList -> {
                    updateDB(courseList);
                    mCourses = courseList;
                    renderCourseView(courseList);
                    if (handlingRefresh) {
                        handlingRefresh = false;
                        RxBus.getDefault().send(new RefreshFinishEvent(courseList.size() != 0));
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    if (handlingRefresh) {
                        handlingRefresh = false;
                        //没有联网会抛出这个异常
                            RxBus.getDefault().send(new RefreshFinishEvent(false
                                    , RefreshFinishEvent.SELF_DEFINE_CODE));
                            retryLoadTable();

                    }
                    //在没有联网的时候 会有一个unknownhost异常
                    try {
                     int code = ((HttpException) throwable).code();
                        if (code == 401) {
                            RxBus.getDefault().send(new RefreshFinishEvent(false
                                    , code));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                });
    }

    /**
     * 渲染显示的课程： 更改日期，显示课程
     */
    private void renderCourseView(List<Course> courseList) {
        mTimetable.getWeekLayout().setWeekDate(selectedWeek - curWeek);
        mTimetable.getTableContent().updateCourses(courseList, selectedWeek);
    }

    private void updateDB(List<Course> courseList) {
        dao.deleteAllCourse();
        for (Course course : courseList) {
            dao.insertCourse(course);
        }
    }
    //设置选择的view位置颠倒
    private void rotateSelectView() {
        if (!selectedIvStatus) {
            mIvSelectWeek.setRotation(180);
        } else {
            mIvSelectWeek.setRotation(0);
        }
        selectedIvStatus = !selectedIvStatus;
    }
    /**
     * @param selectingWeek 从1开始计数
     */
    //第n周
    public void setSelectedWeek(int selectingWeek) {
        mTvSelectWeek.setText(String.format("第%d周", selectingWeek));
    }
    /**
     * @param week 从1开始计数
     */
    public void setCurweek(int week) {
        mTvCurrentWeek.setText("当前周设置为" + week);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (PreferenceUtil.getBoolean(PreferenceUtil.IS_FIRST_ENTER_TABLE, true)) {
            PreferenceUtil.saveBoolean(PreferenceUtil.IS_FIRST_ENTER_TABLE, false);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this);
            ft.attach(this);

            ft.commitAllowingStateLoss();
        }
    }
    @OnClick({R.id.tv_select_week, R.id.iv_select_week})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_week:
            case R.id.iv_select_week:
                if (!selectedIvStatus) {
                    mWeekSelectedView.slideDown();
                    AppStaticUtils.onEvent(getActivity(),"week_select");
                    mWeekSelectedView.setSelectedWeek(selectedWeek);
                    PreferenceUtil.saveInt(PreferenceUtil.CURWEEK,selectedWeek);
                    mShadeView.setVisibility(View.VISIBLE);
                } else {
                    mWeekSelectedView.slideUp();
                    mShadeView.setVisibility(View.GONE);
                }
                rotateSelectView();
                break;
        }
    }


}
